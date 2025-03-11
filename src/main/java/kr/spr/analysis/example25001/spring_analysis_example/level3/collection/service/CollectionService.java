package kr.spr.analysis.example25001.spring_analysis_example.level3.collection.service;

import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.ExampleMusicInfoGroupEnum;
import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.SystemMessageEnum;
import kr.spr.analysis.example25001.spring_analysis_example.level1.util.SystemMessageUtil;
import kr.spr.analysis.example25001.spring_analysis_example.level3._tool.service.WebClientService;
import kr.spr.analysis.example25001.spring_analysis_example.level3.collection.dto.target.CollectionYoutubeItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

// COLLECTION
@Service
public class CollectionService {

    private final Object lock = new Object(); // 쓰레드 제어

    private final RestTemplate restTemplate = new RestTemplate();

    private boolean isFirstStart = true;

    private boolean isOnRefreshCollection = false;

    private long lastRefreshCollectionSystemTime = System.currentTimeMillis();

    private long lastRefreshCollectionCount = 0;

    private List<CollectionYoutubeItemDTO> targetNowCachingList;

    // 값
    @Value("${youtube.config.url}")
    private String baseUrl;

    @Value("${youtube.config.api-key}")
    private String baseApiKey;  // a

    // 서비스
    @Autowired
    private WebClientService webClientService = new WebClientService();

    // 스케줄러
    // COLLECTION_001
    // 기존 운영 사이트 데이터베이스 수집 기능 => 예시로 유튜브 뮤직 인기순 탑100 리스트가 target001 서버에 보관되어 있어서
    @Async("highPriorityTaskExecutor") // 최상위 쓰레드 작동 처리
    @Scheduled(fixedDelay = 30000, initialDelay = 1000)
    // 스케줄러 종료 후 30초 간격으로 다시 시작, 시작 1초 딜레이
    public void refreshCollectionList() {

        String methodId = "COLLECTION_001";
        String refreshCountText = String.format("%09d",
            lastRefreshCollectionCount);
        String messageId = methodId + "_" + refreshCountText;

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


        SystemMessageUtil.printSystemMessage(
            SystemMessageEnum.INFO,
            messageId
                + ": refreshCollectionList -> start"
        );

        synchronized (lock) { // 상태 충돌 방지를 위해 동기화

            if (!isFirstStart) {

                if (nowSystemTime - lastRefreshCollectionSystemTime < intervalRefreshSystemTime) {

                    long intervalCollectionTime =
                        nowSystemTime - lastRefreshCollectionSystemTime;

                    SystemMessageUtil.printSystemMessage(
                        SystemMessageEnum.WARN,
                        messageId
                            + ": 실행시간 미충족 -> " + intervalCollectionTime
                    );

                    return;
                }
                if (isOnRefreshCollection) {

                    SystemMessageUtil.printSystemMessage(
                        SystemMessageEnum.WAIT,
                        messageId
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

            getListCollectionData();

        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException(
                SystemMessageEnum.EXCEPTION,
                messageId
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
            messageId
                + ": refreshCollectionList -> end"
        );
    }

    private void getListCollectionData() {

        // 준비
        ExampleMusicInfoGroupEnum[] enumArray = ExampleMusicInfoGroupEnum.values();
        List<CollectionYoutubeItemDTO> collectionYoutubeItemDTOList = new ArrayList<>();

        // 시작
        // getListCollectionData_001
        // 기존 서버 음악 장르별 한국 인기순 탑 100위 장르별 리스트 호출 (현재 KPOP, JPOP)
        for (ExampleMusicInfoGroupEnum exampleMusicInfoGroupEnumItem : enumArray) {
            if (exampleMusicInfoGroupEnumItem != ExampleMusicInfoGroupEnum.ETC) {

                String webclientInfoUrl = "?part=snippet&type=playlist&q=" + exampleMusicInfoGroupEnumItem.name()
                    + "&maxResults=100&order=relevance&key=" + baseApiKey;

                CollectionYoutubeItemDTO collectionYoutubeItemDTO = webClientService.getWebClientData(
                    CollectionYoutubeItemDTO.class,
                    baseUrl,
                    webclientInfoUrl
                );

                collectionYoutubeItemDTOList.add(collectionYoutubeItemDTO);

            }
        }

        // getListCollectionData_002
        // 현재 캐싱 리스트에 보관하기
        if (!collectionYoutubeItemDTOList.isEmpty()) {

            if (targetNowCachingList == null) {

                targetNowCachingList = collectionYoutubeItemDTOList;

            } else {

                if (!targetNowCachingList.equals(collectionYoutubeItemDTOList)) {

                    targetNowCachingList = collectionYoutubeItemDTOList;
                }

            }
        }
    }

}
