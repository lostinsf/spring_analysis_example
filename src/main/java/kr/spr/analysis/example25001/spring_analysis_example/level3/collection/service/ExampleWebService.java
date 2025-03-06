package kr.spr.analysis.example25001.spring_analysis_example.level3.collection.service;

import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.SystemMessageEnum;
import kr.spr.analysis.example25001.spring_analysis_example.level1.util.SystemMessageUtil;
import kr.spr.analysis.example25001.spring_analysis_example.level3._tool.service.WebClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

// COLLECTION
@Service
public class ExampleWebService {

    private final Object lock = new Object(); // 쓰레드 제어

    private LocalDateTime refreshDateTime;

    private boolean isFirstStart = true;

    private boolean isOnRefreshCollection = false;

    private long lastRefreshCollectionSystemTime = System.currentTimeMillis();

    private long lastRefreshCollectionCount = 0;

    // 값
    @Value("${analysis.webclient.url.example:#{null}}")
    private String exampleUrl;

    // 서비스
    @Autowired
    private WebClientService webClientService = new WebClientService();

    // 스케줄러
    // COLLECTION_001
    @Async("highPriorityTaskExecutor") // 최상위 쓰레드 작동 처리
    @Scheduled(fixedDelay = 30000, initialDelay = 1000) // 스케줄러 종료 후 30초 간격으로 다시 시작, 시작 1초 딜레이
    public void refreshCollectionList() {

        // 준비
        long nowSystemTime = System.currentTimeMillis();
        long intervalRefreshSystemTime = 30000;

        // 강제 검사확인 정보 체크
        LocalDateTime currentTime = LocalDateTime.now();

        // long 데이터 이상범위 초기화
        if (this.lastRefreshCollectionCount > 1000000000) {
            this.lastRefreshCollectionCount = this.lastRefreshCollectionCount % 1000000000;
        }

        // 카운트 증가
        this.lastRefreshCollectionCount++;

        String refreshCountText = String.format("%09d", this.lastRefreshCollectionCount);

        SystemMessageUtil.printSystemMessage(
                SystemMessageEnum.INFO, "COLLECTION_001_" + refreshCountText
                        + ": refreshCollectionList -> start"
        );

        synchronized (this.lock) { // 상태 충돌 방지를 위해 동기화

            if (!this.isFirstStart) {

                if (nowSystemTime - this.lastRefreshCollectionSystemTime < intervalRefreshSystemTime) {

                    long intervalCollectionTime = nowSystemTime - this.lastRefreshCollectionSystemTime;

                    SystemMessageUtil.printSystemMessage(
                            SystemMessageEnum.WARN, "COLLECTION_001_" + refreshCountText
                                    + ": 실행시간 미충족 -> " + intervalCollectionTime
                    );

                    return;
                }
                if (this.isOnRefreshCollection) {

                    SystemMessageUtil.printSystemMessage(
                            SystemMessageEnum.WAIT, "COLLECTION_" + refreshCountText
                                    + ": 현재 refreshCollectionList 동작 중 입니다."
                    );

                    return;
                }

            } else {

                this.isFirstStart = false;
            }

            this.isOnRefreshCollection = true;
        }

        // 수집 로직 체크
        try {

            // TODO 수집 함수 기능 추가
            System.out.println("TODO 수집 함수 기능 추가!");

        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException(
                    SystemMessageEnum.EXCEPTION, "COLLECTION_" + refreshCountText
                            + ": 수집 함수 실행중 에러가 발생 하였습니다.", e
            );

        } finally {

            // 작업 완료 후 상태 초기화 (중복실행 방지)
            synchronized (this.lock) {
                this.isOnRefreshCollection = false; // 업데이트 완료 상태
                this.lastRefreshCollectionSystemTime = nowSystemTime; // 마지막 실행 시간 갱신
                this.lock.notifyAll(); // 대기 중인 다른 스레드 깨우기
            }
        }

        SystemMessageUtil.printSystemMessage(
                SystemMessageEnum.INFO, "COLLECTION_001_" + refreshCountText
                        + ": refreshCollectionList -> end"
        );
    }

}