package kr.spr.analysis.example25001.spring_analysis_example.level3.load.service;

import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.SystemMessageEnum;
import kr.spr.analysis.example25001.spring_analysis_example.level1.util.SystemMessageUtil;
import kr.spr.analysis.example25001.spring_analysis_example.level2.crud.entity.ExampleMusicInfoEntity;
import kr.spr.analysis.example25001.spring_analysis_example.level2.crud.repository.ExampleMusicInfoRepository;
import kr.spr.analysis.example25001.spring_analysis_example.level3.load.dto.LoadMusicInfoListRequestDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.load.dto.LoadMusicInfoListResponseDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.load.dto.obj.LoadMusicInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoadService {

    @Autowired
    private ExampleMusicInfoRepository exampleMusicInfoRepository;

    // 매서드
    public LoadMusicInfoListResponseDTO getLoadMusicInfoList(LoadMusicInfoListRequestDTO loadMusicInfoListRequestDTO) {

        // 준비
        LoadMusicInfoListResponseDTO loadMusicInfoListResponseDTO = new LoadMusicInfoListResponseDTO();
        List<LoadMusicInfoDTO> loadMusicInfoDTOList = new ArrayList<>();

        // 시작
        // getLoadMusicInfoList_001
        // 뮤직 리스트 검색
        try {

            Pageable pageable = PageRequest.of(
                    loadMusicInfoListRequestDTO.getPageNumber(), loadMusicInfoListRequestDTO.getPageCount()
            );

            Page<ExampleMusicInfoEntity> exampleMusicInfoEntityPage = exampleMusicInfoRepository.findByDeletedDateTimeIsNull(pageable);

            _addListLoadMusicInfoDTOFromEntityPage(loadMusicInfoDTOList, exampleMusicInfoEntityPage);

            loadMusicInfoListResponseDTO.setLoadMusicInfoList(loadMusicInfoDTOList);

        } catch (Exception e) {

            SystemMessageUtil.printSystemMessageAndException(
                    SystemMessageEnum.EXCEPTION, ": getLoadMusicInfoList_001 ", e
            );

            // 실패
            return null;
        }

        // 성공
        return loadMusicInfoListResponseDTO;
    }

    private void _addListLoadMusicInfoDTOFromEntityPage(
            List<LoadMusicInfoDTO> loadMusicInfoDTOList, Page<ExampleMusicInfoEntity> exampleMusicInfoEntityPage) {

        for (ExampleMusicInfoEntity exampleMusicInfoEntityItem : exampleMusicInfoEntityPage) {

            LoadMusicInfoDTO loadMusicInfoDTO = new LoadMusicInfoDTO();

            loadMusicInfoDTO.setTitle(
                    exampleMusicInfoEntityItem.getTitle()
            );

            loadMusicInfoDTO.setArtistName(
                    exampleMusicInfoEntityItem.getArtistName()
            );

            loadMusicInfoDTOList.add(loadMusicInfoDTO);
        }
    }
}
