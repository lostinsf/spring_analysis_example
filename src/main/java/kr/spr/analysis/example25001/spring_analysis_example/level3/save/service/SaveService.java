package kr.spr.analysis.example25001.spring_analysis_example.level3.save
    .service;

import jakarta.persistence.OptimisticLockException;
import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.SystemMessageEnum;
import kr.spr.analysis.example25001.spring_analysis_example.level1.util.SystemMessageUtil;
import kr.spr.analysis.example25001.spring_analysis_example.level2.crud.entity.ExampleMusicInfoEntity;
import kr.spr.analysis.example25001.spring_analysis_example.level2.crud.entity.ExampleMusicPlayListInfoEntity;
import kr.spr.analysis.example25001.spring_analysis_example.level2.crud.repository.ExampleMusicInfoRepository;
import kr.spr.analysis.example25001.spring_analysis_example.level2.crud.repository.ExampleMusicPlayListInfoRepository;
import kr.spr.analysis.example25001.spring_analysis_example.level3.collection.dto.CollectionYoutubeItemListDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.collection.dto.obj.CollectionYoutubeItemDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.save.dto.SaveMusicInfoListStoreRequestDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.save.dto.SaveMusicInfoListStoreResponseDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.save.dto.obj.SaveMusicInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaveService {

    @Autowired
    private ExampleMusicInfoRepository exampleMusicInfoRepository;

    @Autowired
    private ExampleMusicPlayListInfoRepository exampleMusicPlayListInfoRepository;

    // 자체 스케줄러 저장 기능
    @Retryable(
        maxAttempts = 3,                      // 최대 3번 시도
        backoff = @Backoff(delay = 100)       // 1초 대기 후 재시도
    )
    @Transactional
    public void syncStatsCachingList(
        List<CollectionYoutubeItemListDTO> targetCachingList) {

        // 준비
        List<ExampleMusicPlayListInfoEntity> createExampleMusicPlayListInfoEntityList = new ArrayList<>();
        boolean isSameData = true;

        // 시작
        // syncStatsCachingList_001
        // 기존 서버에 음악 대상 리스트 임시 저장
        try {

            for (CollectionYoutubeItemListDTO collectionYoutubeItemListDTOItem : targetCachingList) {

                for (CollectionYoutubeItemDTO collectionYoutubeItemDTOItem :
                    collectionYoutubeItemListDTOItem.getItems()) {

                    Pageable pageable = PageRequest.of(0,
                        1);

                    Page<ExampleMusicPlayListInfoEntity> exampleMusicPlayListInfoEntityPage =
                        exampleMusicPlayListInfoRepository
                            .findByPlaylistIdCodeAndDeletedDateTimeIsNullOrderByIdDesc(
                                collectionYoutubeItemDTOItem.getId().getPlaylistId(),
                                pageable);

                    if (exampleMusicPlayListInfoEntityPage.hasContent()) {

                        // TODO 해당 신규 데이터가 데이터베이스 값에 있는지 체크 할 것


                    } else {

                        // 신규로 넣기
                        _addListMySiteUserMusicInfoEntityFromEntityPageAndDTO(
                            collectionYoutubeItemListDTOItem.getGroupEnum(),
                            exampleMusicPlayListInfoEntityPage,
                            collectionYoutubeItemDTOItem,
                            createExampleMusicPlayListInfoEntityList
                        );
                    }

                }
            }


        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException(
                SystemMessageEnum.EXCEPTION,
                ": syncStatsCachingList_001",
                e
            );

            // 실패
            return;

        }

        // syncStatsCachingList_002
        // 데이터가 같지 않으면 신규값으로 최신화 저장
        try {

            if (!isSameData) {

                if (!createExampleMusicPlayListInfoEntityList.isEmpty()) {

                    exampleMusicPlayListInfoRepository.saveAll(createExampleMusicPlayListInfoEntityList);
                }

            }


        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException(
                SystemMessageEnum.EXCEPTION,
                ": syncStatsCachingList_003",
                e
            );

            // 실패
            return;
        }


        // 완료
        // 성공
        return;
    }

    @Recover
    public void recoverSyncStatsCachingList(OptimisticLockException e) {

        SystemMessageUtil.printSystemMessageAndException(
            SystemMessageEnum.EXCEPTION,
            ": recoverSyncStatsCachingList",
            e
        );
    }

    private void _addListMySiteUserMusicInfoEntityFromEntityPageAndDTO(String groupEnum,
                                                                       Page<ExampleMusicPlayListInfoEntity> exampleMusicPlayListInfoEntityPages,
                                                                       CollectionYoutubeItemDTO collectionYoutubeItemDTOItem,
                                                                       List<ExampleMusicPlayListInfoEntity> exampleMusicPlayListInfoEntityList) {
        // 시작
        ExampleMusicPlayListInfoEntity exampleMusicPlayListInfoEntity = new ExampleMusicPlayListInfoEntity();

        // 생성 작업 진행
        if (exampleMusicPlayListInfoEntityPages == null) {
            exampleMusicPlayListInfoEntity
                = __addMySiteUserMusicInfoEntityFromDTOAndPage(
                groupEnum,
                collectionYoutubeItemDTOItem
                ,
                null
            );
        }
        // 수정 작업 진행
        else {

            exampleMusicPlayListInfoEntity
                = __addMySiteUserMusicInfoEntityFromDTOAndPage(
                groupEnum,
                collectionYoutubeItemDTOItem
                ,
                exampleMusicPlayListInfoEntityPages
            );

        }

        exampleMusicPlayListInfoEntityList.add(exampleMusicPlayListInfoEntity);

    }

    private ExampleMusicPlayListInfoEntity __addMySiteUserMusicInfoEntityFromDTOAndPage(
        String groupEnum,
        CollectionYoutubeItemDTO collectionYoutubeItemDTO,
        Page<ExampleMusicPlayListInfoEntity> exampleMusicPlayListInfoEntityPages
    ) {

        ExampleMusicPlayListInfoEntity exampleMusicPlayListInfoEntity = new ExampleMusicPlayListInfoEntity();

        // 수정일 경우
        if (exampleMusicPlayListInfoEntityPages != null) {
            exampleMusicPlayListInfoEntity
                = exampleMusicPlayListInfoEntityPages.getContent().getFirst();

        }

        exampleMusicPlayListInfoEntity.setGroupEnum(groupEnum);

        exampleMusicPlayListInfoEntity.setPlayListIdCode(
            collectionYoutubeItemDTO.getId().getPlaylistId()
        );

        exampleMusicPlayListInfoEntity.setPlayListTitle(
            collectionYoutubeItemDTO.getSnippet().getTitle()
        );

        exampleMusicPlayListInfoEntity.setChannelIdCode(
            collectionYoutubeItemDTO.getSnippet().getChannelId()
        );

        exampleMusicPlayListInfoEntity.setChannelTitle(
            collectionYoutubeItemDTO.getSnippet().getChannelTitle()
        );

        exampleMusicPlayListInfoEntity.setDescription(
            collectionYoutubeItemDTO.getSnippet().getDescription()
        );

        return exampleMusicPlayListInfoEntity;

    }

    // save_001
    @Retryable(
        maxAttempts = 3,                      // 최대 3번 시도
        backoff = @Backoff(delay = 100)       // 1초 대기 후 재시도
    )
    @Transactional
    public SaveMusicInfoListStoreResponseDTO storeSaveMusicInfoList
    (SaveMusicInfoListStoreRequestDTO data) {

        // 준비
        SaveMusicInfoListStoreResponseDTO saveMusicInfoListStoreResponseDTO =
            new SaveMusicInfoListStoreResponseDTO();
        List<ExampleMusicInfoEntity> createExampleMusicInfoEntityList =
            new ArrayList<>();
        List<ExampleMusicInfoEntity> updateExampleMusicInfoEntityList =
            new ArrayList<>();
        List<SaveMusicInfoDTO> saveMusicInfoDTOList =
            data.getSaveMusicInfoList();

        // 시작
        // setSaveMusicInfoList_001
        // 해당 조건이 있는지 검색
        try {

            for (SaveMusicInfoDTO saveMusicInfoDTOItem : saveMusicInfoDTOList) {

                Pageable pageable = PageRequest.of(0,
                    1);

                Page<ExampleMusicInfoEntity> exampleMusicInfoEntityPage =
                    exampleMusicInfoRepository
                        .findByTitleAndArtistNameAndDeletedDateTimeIsNullOrderById(saveMusicInfoDTOItem.getMusicTitle(),
                            saveMusicInfoDTOItem.getArtistName(),
                            pageable);

                _addListExampleMusicInfoEntityFromEntityPageAndDTO(
                    exampleMusicInfoEntityPage,
                    saveMusicInfoDTOItem,
                    createExampleMusicInfoEntityList,
                    updateExampleMusicInfoEntityList);

            }

        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException
                (SystemMessageEnum.EXCEPTION,
                    ": setSaveMusicInfoList_001",
                    e);

            // 실패
            return saveMusicInfoListStoreResponseDTO;
        }

        // setSaveMusicInfoList_002
        // 조건이 없다면 생성
        try {

            if (!createExampleMusicInfoEntityList.isEmpty()) {

                exampleMusicInfoRepository.saveAll
                    (createExampleMusicInfoEntityList);

            }


        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException
                (SystemMessageEnum.EXCEPTION,
                    ": setSaveMusicInfoList_002",
                    e);

            // 실패
            return saveMusicInfoListStoreResponseDTO;
        }

        // setSaveMusicInfoList_003
        // 조건이 있다면 updateExampleMusicInfoEntityList 수정
        try {

            if (!updateExampleMusicInfoEntityList.isEmpty()) {

                exampleMusicInfoRepository.saveAll
                    (updateExampleMusicInfoEntityList);
            }


        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException
                (SystemMessageEnum.EXCEPTION,
                    ": setSaveMusicInfoList_003",
                    e);

            // 실패
            return saveMusicInfoListStoreResponseDTO;
        }

        // 성공
        saveMusicInfoListStoreResponseDTO.setStatus(SystemMessageEnum.SUCCESS);
        return saveMusicInfoListStoreResponseDTO;
    }

    @Recover
    public void recoverSetSaveMusicInfoList(OptimisticLockException e) {

        SystemMessageUtil.printSystemMessageAndException(
            SystemMessageEnum.EXCEPTION,
            ": recoverSetSaveMusicInfoList",
            e
        );
    }

    private void _addListExampleMusicInfoEntityFromEntityPageAndDTO(
        Page<ExampleMusicInfoEntity> exampleMusicInfoEntityPage,
        SaveMusicInfoDTO saveMusicInfoDTOItem,
        List<ExampleMusicInfoEntity> createExampleMusicInfoEntityList,
        List<ExampleMusicInfoEntity> updateExampleMusicInfoEntityList) {

        if (exampleMusicInfoEntityPage == null) {
            return;
        }

        if (saveMusicInfoDTOItem == null) {
            return;
        }

        if (createExampleMusicInfoEntityList == null) {
            return;
        }

        if (exampleMusicInfoEntityPage.hasContent() && updateExampleMusicInfoEntityList != null) {

            ExampleMusicInfoEntity exampleMusicInfoEntity = exampleMusicInfoEntityPage.getContent().getFirst();

            exampleMusicInfoEntity.setUpdatedDateTime(
                LocalDateTime.now()
            );

            updateExampleMusicInfoEntityList.add(exampleMusicInfoEntity);
        } else {

            ExampleMusicInfoEntity exampleMusicInfoEntity = new ExampleMusicInfoEntity();

            exampleMusicInfoEntity.setArtistName(saveMusicInfoDTOItem.getArtistName());
            exampleMusicInfoEntity.setMusicTitle(saveMusicInfoDTOItem.getMusicTitle());

            createExampleMusicInfoEntityList.add(exampleMusicInfoEntity);
        }
    }

    private void _addListExampleMusicInfoEntityFromEntityPageAndDTO(
        Page<ExampleMusicInfoEntity> exampleMusicInfoEntityPage,
        SaveMusicInfoDTO saveMusicInfoDTOItem,
        List<ExampleMusicInfoEntity> exampleMusicInfoEntityList) {

        _addListExampleMusicInfoEntityFromEntityPageAndDTO(exampleMusicInfoEntityPage,
            saveMusicInfoDTOItem,
            exampleMusicInfoEntityList,
            null);

    }

    // save_002
    public SaveMusicInfoListStoreResponseDTO deleteSaveMusicInfoList(SaveMusicInfoListStoreRequestDTO data) {

        // 준비
        SaveMusicInfoListStoreResponseDTO saveMusicInfoListStoreResponseDTO =
            new SaveMusicInfoListStoreResponseDTO();
        List<ExampleMusicInfoEntity> deleteExampleMusicInfoEntityList =
            new ArrayList<>();
        List<SaveMusicInfoDTO> saveMusicInfoDTOList =
            data.getSaveMusicInfoList();

        // 시작
        // deleteSaveMusicInfoList_001
        // 삭제 대상 리스트 검색
        try {

            for (SaveMusicInfoDTO saveMusicInfoDTOItem : saveMusicInfoDTOList) {

                Pageable pageable = PageRequest.of(0,
                    1);

                Page<ExampleMusicInfoEntity> exampleMusicInfoEntityPage =
                    exampleMusicInfoRepository
                        .findByTitleAndArtistNameAndDeletedDateTimeIsNullOrderById(saveMusicInfoDTOItem.getMusicTitle(),
                            saveMusicInfoDTOItem.getArtistName(),
                            pageable);

                if (exampleMusicInfoEntityPage.hasContent()) {

                    _addListExampleMusicInfoEntityFromEntityPageAndDTO(
                        exampleMusicInfoEntityPage,
                        saveMusicInfoDTOItem,
                        deleteExampleMusicInfoEntityList);
                } else {

                    SystemMessageUtil.printSystemMessage(
                        SystemMessageEnum.EMPTY,
                        ": deleteSaveMusicInfoList_001"
                    );

                    // 실패
                    saveMusicInfoListStoreResponseDTO.setStatus(SystemMessageEnum.EMPTY);
                    return saveMusicInfoListStoreResponseDTO;
                }

            }


        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException(
                SystemMessageEnum.EXCEPTION,
                ": deleteSaveMusicInfoList_001",
                e
            );

            // 실패
            return saveMusicInfoListStoreResponseDTO;

        }

        // deleteSaveMusicInfoList_002
        // 삭제시간 업데이트
        try {

            if (!deleteExampleMusicInfoEntityList.isEmpty()) {

                LocalDateTime _now = LocalDateTime.now();

                for (ExampleMusicInfoEntity exampleMusicInfoEntity :
                    deleteExampleMusicInfoEntityList) {

                    exampleMusicInfoEntity.setDeletedDateTime(_now);
                }

            }

        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException(
                SystemMessageEnum.EXCEPTION,
                ": deleteSaveMusicInfoList_002",
                e
            );

            // 실패
            return saveMusicInfoListStoreResponseDTO;

        }

        // deleteSaveMusicInfoList_003
        // 삭제시간 저장
        try {

            if (!deleteExampleMusicInfoEntityList.isEmpty()) {

                exampleMusicInfoRepository.saveAll(
                    deleteExampleMusicInfoEntityList
                );

            }

        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException(
                SystemMessageEnum.EXCEPTION,
                ": deleteSaveMusicInfoList_003",
                e
            );

            // 실패
            return saveMusicInfoListStoreResponseDTO;

        }

        // 성공
        saveMusicInfoListStoreResponseDTO.setStatus(SystemMessageEnum.SUCCESS);
        return saveMusicInfoListStoreResponseDTO;
    }

}
