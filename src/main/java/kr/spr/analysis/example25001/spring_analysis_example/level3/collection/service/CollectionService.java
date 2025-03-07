package kr.spr.analysis.example25001.spring_analysis_example.level3.collection.service;

import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.SystemMessageEnum;
import kr.spr.analysis.example25001.spring_analysis_example.level1.util.SystemMessageUtil;
import kr.spr.analysis.example25001.spring_analysis_example.level3._tool.service.WebClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

// COLLECTION
@Service
public class CollectionService {

    private final Object lock = new Object(); // 쓰레드 제어

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
    @Scheduled(fixedDelay = 30000, initialDelay = 1000)
    // 스케줄러 종료 후 30초 간격으로 다시 시작, 시작 1초 딜레이
    public void refreshCollectionList() {

        String methodId1 = "COLLECTION_001";

        // 준비
        long nowSystemTime = System.currentTimeMillis();
        long intervalRefreshSystemTime = 30000;

        // long 데이터 이상범위 초기화
        if (lastRefreshCollectionCount > 1000000000) {
            lastRefreshCollectionCount =
                this.lastRefreshCollectionCount % 1000000000;
        }

        // 카운트 증가
        lastRefreshCollectionCount++;

        String refreshCountText = String.format("%09d",
            lastRefreshCollectionCount);

        SystemMessageUtil.printSystemMessage(
            SystemMessageEnum.INFO,
            methodId1 + refreshCountText
                + ": refreshCollectionList -> start"
        );

        synchronized (lock) { // 상태 충돌 방지를 위해 동기화

            if (!isFirstStart) {

                if (nowSystemTime - lastRefreshCollectionSystemTime < intervalRefreshSystemTime) {

                    long intervalCollectionTime =
                        nowSystemTime - lastRefreshCollectionSystemTime;

                    SystemMessageUtil.printSystemMessage(
                        SystemMessageEnum.WARN,
                        methodId1 + refreshCountText
                            + ": 실행시간 미충족 -> " + intervalCollectionTime
                    );

                    return;
                }
                if (isOnRefreshCollection) {

                    SystemMessageUtil.printSystemMessage(
                        SystemMessageEnum.WAIT,
                        methodId1 + refreshCountText
                            + ": 현재 refreshCollectionList 동작 중"
                    );

                    return;
                }

            } else {

                isFirstStart = false;
            }

            isOnRefreshCollection = true;
        }

        // 수집 로직 체크
        try {

            // TODO 해당 수집 메서드 구현 필요
            _getListCollectionData();

        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException(
                SystemMessageEnum.EXCEPTION,
                methodId1 + refreshCountText
                    + ": 수집 함수 실행중 에러",
                e
            );

        } finally {

            // 작업 완료 후 상태 초기화 (중복실행 방지)
            synchronized (lock) {
                isOnRefreshCollection = false; // 업데이트 완료 상태
                lastRefreshCollectionSystemTime = nowSystemTime; // 마지막 실행 시간 갱신
                lock.notifyAll(); // 대기 중인 다른 스레드 깨우기
            }
        }

        SystemMessageUtil.printSystemMessage(
            SystemMessageEnum.INFO,
            methodId1 + refreshCountText
                + ": refreshCollectionList -> end"
        );
    }

    private void _getListCollectionData() {

        // 수집 대상 서버 json 구조 가져오기
    }

}
