package kr.spr.analysis.example25001.spring_analysis_example.level4.load.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.spr.analysis.example25001.spring_analysis_example.level3._tool.dto.GenericResponseDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.load.dto.LoadMusicInfoListRequestDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.load.dto.LoadMusicInfoListResponseDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.load.service.LoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/load")
@Tag(name = "/load", description = "데이터 로드 API")
public class LoadController {

    // 서비스
    @Autowired
    private LoadService loadService;

    // API
    @GetMapping("/get/music/info/list/get_music_info_list")
    @Operation(summary = ":음악 정보 리스트 검색",
            description = "<b>/load/get/music/info/list/get_music_info_list" +
                    "</b><br><br>" +
                    "<b>1. 아이디:</b> <br>" +
                    "- load_001 <br>" +
                    "<br>" +
                    "<b>2. parameter 설명:</b> <br>" +
                    "- pageNumber : 페이지 위치 <br>" +
                    "- pageCount : 페이지 수 <br>" +
                    "<br>"
    )
    public GenericResponseDTO<LoadMusicInfoListResponseDTO> getListMusicInfoLoad(

            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "20") int pageCount
    ) {
        // 준비
        LoadMusicInfoListResponseDTO loadMusicInfoListResponseDTO;
        LoadMusicInfoListRequestDTO loadMusicInfoListRequestDTO =
                new LoadMusicInfoListRequestDTO();
        loadMusicInfoListRequestDTO.setPageNumber(pageNumber);
        loadMusicInfoListRequestDTO.setPageCount(pageCount);

        // 시작
        loadMusicInfoListResponseDTO =
                loadService.getLoadMusicInfoList(loadMusicInfoListRequestDTO);

        // 완료
        if (loadMusicInfoListResponseDTO.getLoadMusicInfoList().isEmpty()) {

            // 실패
            return GenericResponseDTO.fail();

        }

        // 성공
        return GenericResponseDTO.success(loadMusicInfoListResponseDTO);
    }

}
