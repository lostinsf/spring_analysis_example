package kr.spr.analysis.example25001.spring_analysis_example.level3.collection.service;

import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.SystemMessageEnum;
import kr.spr.analysis.example25001.spring_analysis_example.level1.util.SystemMessageUtil;
import kr.spr.analysis.example25001.spring_analysis_example.level3._tool.service.WebClientService;
import kr.spr.analysis.example25001.spring_analysis_example.level3.collection.dto.target.CollectionYoutubeItemListDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.load.service.LoadService;
import kr.spr.analysis.example25001.spring_analysis_example.level3.refine.service.RefineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
    private RefineService refineService;

    @Autowired
    private LoadService loadService;


    // 스케줄러
    // COLLECTION_001
    // 분석정보 대상 리스트 호출 후 open api 정보 수집
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

            // TODO 분석 대상 리스트 수집 다시 하기
            getListCollectionStats();

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
                lock.notifyAll(); // 대기 중인 다른 스레드 깨우기
            }
        }

        SystemMessageUtil.printSystemMessage(
            SystemMessageEnum.INFO,
            messageId
                + ": refreshCollectionList -> end"
        );
    }

    private void getListCollectionStats() {

        // 준비
//        LoadMusicInfoListRequestDTO loadMusicInfoListRequestDTO = new LoadMusicInfoListRequestDTO();
//        LoadMusicInfoListResponseDTO loadMusicInfoListResponseDTO = new LoadMusicInfoListResponseDTO();

        // 시작
        // 분석 대상 리스트 별 open api에서 정보 추출하기
//        nowLoadMusicInfoDTOList = loadService.getLoadMusicInfoList();
//
//
//        for (ExampleMusicInfoGroupEnum exampleMusicInfoGroupEnumItem : enumArray) {
//            if (exampleMusicInfoGroupEnumItem != ExampleMusicInfoGroupEnum.ETC) {
//
//                String query = songTitle + " " + artistName;
//
//                String webclientInfoUrl =
//                    "?part=snippet&type=video&maxResults=1&q=" + exampleMusicInfoGroupEnumItem.name()
//                        + "&maxResults=100&order=relevance&key=" + baseApiKey;
//
//                CollectionYoutubeItemListDTO collectionYoutubeItemListDTO = webClientService.getWebClientData(
//                    CollectionYoutubeItemListDTO.class,
//                    baseUrl,
//                    webclientInfoUrl
//                );
//
//                nowCollectionYoutubeItemListDTOList.add(collectionYoutubeItemListDTO);
//
//            }
//        }
//
//        // getListCollectionData_002
//        // 현재 캐싱 리스트에 보관하기
//        if (!nowCollectionYoutubeItemListDTOList.isEmpty()) {
//
//            if (targetCachingList == null) {
//
//                targetCachingList = nowCollectionYoutubeItemListDTOList;
//
//            } else {
//
//                isEqualTargetCachingList = !_isEqualTargetListAndNowLists(targetCachingList,
//                    nowCollectionYoutubeItemListDTOList);
//
//
//                // 보관된 수집 데이터가 현재와 같지 않다면 현재로 교체
//                if (!isEqualTargetCachingList) {
//
//                    targetCachingList = nowCollectionYoutubeItemListDTOList;
//
//                    isEqualTargetCachingList = true;
//                }
//
//            }
//
//            // 보관된 수집 데이터가 현재와 같지 않다면, 정제 서비스에 보관
//            if (!isEqualTargetCachingList) {
//                refineService.refineTargetCachingList(
//                    targetCachingList
//                );
//            }
//        }
    }

    private boolean _isEqualTargetListAndNowLists(List<CollectionYoutubeItemListDTO> targetNowCachingList,
                                                  List<CollectionYoutubeItemListDTO> nowCollectionYoutubeItemListDTOList) {

        // null 체크
        if (targetNowCachingList == null || nowCollectionYoutubeItemListDTOList == null) {

            return false;
        }

        // 길이
        if (targetNowCachingList.size() != nowCollectionYoutubeItemListDTOList.size()) {

            return false;
        }

        // 같은 비교값 체크
        int totalSize = targetNowCachingList.size();
        for (int i = 0; i < totalSize; i++) {

            // null 체크
            if (targetNowCachingList.get(i).getItems() == null
                || nowCollectionYoutubeItemListDTOList.get(i).getItems() == null
            ) {
                return false;
            }

            // id .getPlaylistId() 체크
            String checkText = targetNowCachingList.get(i).getItems().get(i).getId().getPlaylistId();
            String compareText = nowCollectionYoutubeItemListDTOList.get(i).getItems().get(i).getId().getPlaylistId();
            if (!checkText.equals(
                compareText
            )
            ) {
                return false;
            }

            // snippet .getTitle() 체크
            checkText = targetNowCachingList.get(i).getItems().get(i).getSnippet().getTitle();
            compareText = nowCollectionYoutubeItemListDTOList.get(i).getItems().get(i).getSnippet().getTitle();
            if (!checkText.equals(
                compareText
            )
            ) {
                return false;
            }

            // snippet .getTitle() 체크
            checkText = targetNowCachingList.get(i).getItems().get(i).getSnippet().getChannelId();
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
