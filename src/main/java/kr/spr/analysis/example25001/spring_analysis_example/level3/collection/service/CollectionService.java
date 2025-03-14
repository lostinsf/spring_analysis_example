package kr.spr.analysis.example25001.spring_analysis_example.level3.collection.service;

import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.ExampleMusicInfoGroupEnum;
import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.SystemMessageEnum;
import kr.spr.analysis.example25001.spring_analysis_example.level1.util.SystemMessageUtil;
import kr.spr.analysis.example25001.spring_analysis_example.level3._tool.service.WebClientService;
import kr.spr.analysis.example25001.spring_analysis_example.level3.collection.dto.CollectionYoutubeItemListDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.save.service.SaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// COLLECTION
@Service
public class CollectionService {

    private final Object lock = new Object(); // 쓰레드 제어

    private boolean isFirstStart = true;

    private boolean isOnRefreshCollection = false;

    private long lastRefreshCollectionSystemTime = System.currentTimeMillis();

    private long lastRefreshCollectionCount = 0;

    private List<CollectionYoutubeItemListDTO> statsCachingList;

    private boolean isEqualStatsCachingList = false;

    // 값
    @Value("${youtube.config.url}")
    private String baseUrl;

    @Value("${youtube.config.api-key}")
    private String baseApiKey;  // a

    // 서비스
    @Autowired
    private WebClientService webClientService = new WebClientService();

    @Autowired
    private SaveService saveService;


    // 스케줄러
    // COLLECTION_001
    // 분석정보 대상 리스트 호출 후 open api 정보 수집 (예시: 유튜브 뮤직리스트 인기순 탑100 playList 저장)
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

            }

            isOnRefreshCollection = true;
        }

        // 수집 로직 체크
        try {

            scheduledListCollectionStats();

        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException(
                SystemMessageEnum.EXCEPTION,
                messageId
                    + ": 수집 중 에러",
                e
            );

        } finally {

            // 작업 완료 후 상태 초기화 (중복실행 방지)
            synchronized (lock) {
                isOnRefreshCollection = false; // 업데이트 완료 상태
                lastRefreshCollectionSystemTime = nowSystemTime; // 마지막 실행 시간 갱신
                isFirstStart = false;
                lock.notifyAll(); // 대기 중인 다른 스레드 깨우기
            }
        }

        SystemMessageUtil.printSystemMessage(
            SystemMessageEnum.INFO,
            messageId
                + ": refreshCollectionList -> end"
        );
    }

    private void scheduledListCollectionStats() {

        // 준비
        ExampleMusicInfoGroupEnum[] enumArray = ExampleMusicInfoGroupEnum.values();
        List<CollectionYoutubeItemListDTO> nowCollectionYoutubeItemListDTOList = new ArrayList<>();

        // 시작
        // 유튜브 음악 장르별 한국 인기순 탑 50위 장르별 Open API로 리스트 호출
        for (ExampleMusicInfoGroupEnum exampleMusicInfoGroupEnumItem : enumArray) {
            if (exampleMusicInfoGroupEnumItem != ExampleMusicInfoGroupEnum.ETC) {

                String webclientInfoUrl = "?part=snippet&type=playlist&q=" + exampleMusicInfoGroupEnumItem.name()
                    + "&maxResults=100&order=relevance&key=" + baseApiKey;

                CollectionYoutubeItemListDTO collectionYoutubeItemListDTO = webClientService.getWebClientData(
                    CollectionYoutubeItemListDTO.class,
                    baseUrl,
                    webclientInfoUrl
                );

                collectionYoutubeItemListDTO.setGroupEnum(exampleMusicInfoGroupEnumItem);
                nowCollectionYoutubeItemListDTOList.add(collectionYoutubeItemListDTO);

            }
        }

        // 현재 호출한 데이터를 서버내 캐싱 리스트에 저장하기
        if (!nowCollectionYoutubeItemListDTOList.isEmpty()) {

            if (!nowCollectionYoutubeItemListDTOList.getFirst().getItems().isEmpty()) {

                if (statsCachingList == null) {

                    statsCachingList = nowCollectionYoutubeItemListDTOList;

                } else {

                    isEqualStatsCachingList = !_isEqualStatsListAndNowLists(statsCachingList,
                        nowCollectionYoutubeItemListDTOList);


                    // 보관된 수집 데이터가 현재와 같지 않다면 현재로 교체
                    if (!isEqualStatsCachingList) {

                        statsCachingList = nowCollectionYoutubeItemListDTOList;

                        isEqualStatsCachingList = true;

                        SystemMessageUtil.printSystemMessage(
                            SystemMessageEnum.UPDATE,
                            ": scheduledListCollectionStats"
                        );
                    }

                }
            }
        }

        // 보관된 수집 데이터가 최근에 계산한 동일 현재와 같지 않다면, 저장 서비스를 통해 수집한 데이터 보관
        if (!isEqualStatsCachingList) {
            saveService.syncStatsCachingList(
                statsCachingList
            );

            SystemMessageUtil.printSystemMessage(
                SystemMessageEnum.STORE,
                ": scheduledListCollectionStats"
            );
        }
    }

    private boolean _isEqualStatsListAndNowLists(List<CollectionYoutubeItemListDTO> statsNowCachingList,
                                                 List<CollectionYoutubeItemListDTO> nowCollectionYoutubeItemListDTOList) {

        // null 체크
        if (statsNowCachingList == null || nowCollectionYoutubeItemListDTOList == null) {

            return false;
        }

        // 길이
        if (statsNowCachingList.size() != nowCollectionYoutubeItemListDTOList.size()) {

            return false;
        }

        // 같은 비교값 체크
        int totalSize = statsNowCachingList.size();
        for (int i = 0; i < totalSize; i++) {

            // null 체크
            if (statsNowCachingList.get(i).getItems() == null
                || nowCollectionYoutubeItemListDTOList.get(i).getItems() == null
            ) {
                return false;
            }

            // id .getPlaylistId() 체크
            String checkText = statsNowCachingList.get(i).getItems().get(i).getId().getPlaylistId();
            String compareText = nowCollectionYoutubeItemListDTOList.get(i).getItems().get(i).getId().getPlaylistId();
            if (!checkText.equals(
                compareText
            )
            ) {
                return false;
            }

            // snippet .getTitle() 체크
            checkText = statsNowCachingList.get(i).getItems().get(i).getSnippet().getTitle();
            compareText = nowCollectionYoutubeItemListDTOList.get(i).getItems().get(i).getSnippet().getTitle();
            if (!checkText.equals(
                compareText
            )
            ) {
                return false;
            }

            // snippet .getTitle() 체크
            checkText = statsNowCachingList.get(i).getItems().get(i).getSnippet().getChannelId();
            compareText = nowCollectionYoutubeItemListDTOList.get(i).getItems().get(i).getSnippet().getChannelId();
            if (!checkText.equals(
                compareText
            )
            ) {
                return false;
            }

        }

        return true;

    }

}
