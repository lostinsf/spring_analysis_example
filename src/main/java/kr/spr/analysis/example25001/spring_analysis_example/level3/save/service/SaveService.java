package kr.spr.analysis.example25001.spring_analysis_example.level3.save
    .service;

import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.SystemMessageEnum;
import kr.spr.analysis.example25001.spring_analysis_example.level1.util.SystemMessageUtil;
import kr.spr.analysis.example25001.spring_analysis_example.level2.crud.entity.ExampleMusicInfoEntity;
import kr.spr.analysis.example25001.spring_analysis_example.level2.crud.repository.ExampleMusicInfoRepository;
import kr.spr.analysis.example25001.spring_analysis_example.level3.save.dto.SaveMusicInfoListRequestDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.save.dto.SaveMusicInfoListResponseDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.save.dto.obj.SaveMusicInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaveService {

    @Autowired
    private ExampleMusicInfoRepository exampleMusicInfoRepository;

    // save_001
    public SaveMusicInfoListResponseDTO setSaveMusicInfoList
    (SaveMusicInfoListRequestDTO data) {

        // 준비
        SaveMusicInfoListResponseDTO saveMusicInfoListResponseDTO =
            new SaveMusicInfoListResponseDTO();
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
                        .findByTitleAndArtistNameAndDeletedDateTimeIsNullOrderById(saveMusicInfoDTOItem.getTitle(),
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
            return saveMusicInfoListResponseDTO;
        }

        // setSaveMusicInfoList_002
        // 조건이 없다면 생성
        try {

            if (!createExampleMusicInfoEntityList.isEmpty()) {

                if (!_isSaveEntity(createExampleMusicInfoEntityList)) {

                    SystemMessageUtil.printSystemMessage
                        (SystemMessageEnum.UNIQUE_FAIL,
                            ": setSaveMusicInfoList_002");

                    // 실패
                    return saveMusicInfoListResponseDTO;
                }

            }


        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException
                (SystemMessageEnum.EXCEPTION,
                    ": setSaveMusicInfoList_002",
                    e);

            // 실패
            return saveMusicInfoListResponseDTO;
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
            return saveMusicInfoListResponseDTO;
        }

        // 성공
        saveMusicInfoListResponseDTO.setStatus(SystemMessageEnum.SUCCESS);
        return saveMusicInfoListResponseDTO;
    }

    private boolean _isSaveEntity(List<ExampleMusicInfoEntity> createExampleMusicInfoEntityList) {

        boolean saved = false;

        int count = 0;

        while (!saved) {
            try {
                exampleMusicInfoRepository.saveAll(createExampleMusicInfoEntityList);
                saved = true;

            } catch (DataIntegrityViolationException e) {

                // 3회 위상 반복 저장하는데도 불구하고 유니크값이 그대로 일때 체크
                if (count > 3) {
                    return saved;
                }

                // uniqueCode 설정 후 다시 저장
                for (ExampleMusicInfoEntity exampleMusicInfoEntityItem : createExampleMusicInfoEntityList) {

                    exampleMusicInfoEntityItem.setUniqueCode(exampleMusicInfoEntityItem.getUniqueCode());
                }

                count++;

            }
        }

        return saved;
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

            updateExampleMusicInfoEntityList.addAll(exampleMusicInfoEntityPage.getContent());
        } else {

            ExampleMusicInfoEntity exampleMusicInfoEntity =
                new ExampleMusicInfoEntity();

            exampleMusicInfoEntity.setArtistName(saveMusicInfoDTOItem.getArtistName());
            exampleMusicInfoEntity.setTitle(saveMusicInfoDTOItem.getTitle());

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
    public SaveMusicInfoListResponseDTO deleteSaveMusicInfoList(SaveMusicInfoListRequestDTO data) {

        // 준비
        SaveMusicInfoListResponseDTO saveMusicInfoListResponseDTO =
            new SaveMusicInfoListResponseDTO();
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
                        .findByTitleAndArtistNameAndDeletedDateTimeIsNullOrderById(saveMusicInfoDTOItem.getTitle(),
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
                    saveMusicInfoListResponseDTO.setStatus(SystemMessageEnum.EMPTY);
                    return saveMusicInfoListResponseDTO;
                }

            }


        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException(
                SystemMessageEnum.EXCEPTION,
                ": deleteSaveMusicInfoList_001",
                e
            );

            // 실패
            return saveMusicInfoListResponseDTO;

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
            return saveMusicInfoListResponseDTO;

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
            return saveMusicInfoListResponseDTO;

        }

        // 성공
        saveMusicInfoListResponseDTO.setStatus(SystemMessageEnum.SUCCESS);
        return saveMusicInfoListResponseDTO;
    }

}
