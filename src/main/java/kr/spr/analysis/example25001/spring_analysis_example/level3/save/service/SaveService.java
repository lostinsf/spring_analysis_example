package kr.spr.analysis.example25001.spring_analysis_example.level3.save.service;

import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.SystemMessageEnum;
import kr.spr.analysis.example25001.spring_analysis_example.level1.util.SystemMessageUtil;
import kr.spr.analysis.example25001.spring_analysis_example.level2.crud.entity.ExampleMusicInfoEntity;
import kr.spr.analysis.example25001.spring_analysis_example.level2.crud.repository.ExampleMusicInfoRepository;
import kr.spr.analysis.example25001.spring_analysis_example.level3.save.dto.SaveMusicInfoListRequestDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.save.dto.SaveMusicInfoListResponseDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.save.dto.obj.SaveMusicInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaveService {

    @Autowired
    private ExampleMusicInfoRepository exampleMusicInfoRepository;

    public SaveMusicInfoListResponseDTO setSaveMusicInfoList(SaveMusicInfoListRequestDTO data) {

        // 준비
        SaveMusicInfoListResponseDTO saveMusicInfoListResponseDTO =
            new SaveMusicInfoListResponseDTO();
        List<ExampleMusicInfoEntity> createExampleMusicInfoEntityList =
            new ArrayList<>();
        List<ExampleMusicInfoEntity> updateExampleMusicInfoEntityList =
            new ArrayList<>();

        // 시작
        // setSaveMusicInfoList_001
        // 해당 조건이 있는지 검색
        try {

            for (SaveMusicInfoDTO saveMusicInfoDTOItem :
                data.getSaveMusicInfoList()) {

                Pageable pageable = PageRequest.of(1,
                    1);

                Page<ExampleMusicInfoEntity> exampleMusicInfoEntityPage =
                    exampleMusicInfoRepository.findByTitleAndArtistNameAndDeletedDateTimeIsNull(saveMusicInfoDTOItem.getTitle(),
                    saveMusicInfoDTOItem.getArtistName(),
                    pageable);

                _addListExampleMusicInfoEntityFromEntityPageAndDTO(createExampleMusicInfoEntityList,
                    updateExampleMusicInfoEntityList,
                    exampleMusicInfoEntityPage,
                    saveMusicInfoDTOItem);

            }

        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException(SystemMessageEnum.EXCEPTION,
                ": setSaveMusicInfoList_001",
                e);

            // 실패
            return null;
        }

        // setSaveMusicInfoList_002
        // 조건이 없다면 생성
        try {

            if (!createExampleMusicInfoEntityList.isEmpty()) {

                exampleMusicInfoRepository.saveAll(createExampleMusicInfoEntityList);
            }


        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException(SystemMessageEnum.EXCEPTION,
                ": setSaveMusicInfoList_002",
                e);

            // 실패
            return null;
        }

        // setSaveMusicInfoList_003
        // 조건이 있다면 updateExampleMusicInfoEntityList 수정
        try {

            if (!updateExampleMusicInfoEntityList.isEmpty()) {

                exampleMusicInfoRepository.saveAll(updateExampleMusicInfoEntityList);
            }


        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException(SystemMessageEnum.EXCEPTION,
                ": setSaveMusicInfoList_003",
                e);

            // 실패
            return null;
        }

        return saveMusicInfoListResponseDTO;
    }

    private void _addListExampleMusicInfoEntityFromEntityPageAndDTO(List<ExampleMusicInfoEntity> createExampleMusicInfoEntityList,
                                                                    List<ExampleMusicInfoEntity> updateExampleMusicInfoEntityList,
                                                                    Page<ExampleMusicInfoEntity> exampleMusicInfoEntityPage,
                                                                    SaveMusicInfoDTO saveMusicInfoDTOItem) {

        if (exampleMusicInfoEntityPage.hasContent()) {

            updateExampleMusicInfoEntityList.addAll(exampleMusicInfoEntityPage.getContent());
        } else {

            ExampleMusicInfoEntity exampleMusicInfoEntity =
                new ExampleMusicInfoEntity();

            exampleMusicInfoEntity.setArtistName(saveMusicInfoDTOItem.getArtistName());
            exampleMusicInfoEntity.setTitle(saveMusicInfoDTOItem.getTitle());

            createExampleMusicInfoEntityList.add(exampleMusicInfoEntity);
        }
    }

}
