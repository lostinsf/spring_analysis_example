package kr.spr.analysis.example25001.spring_analysis_example.level4.save.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.spr.analysis.example25001.spring_analysis_example.level3._tool.dto.GenericResponseDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.save.dto.SaveMusicInfoListRequestDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.save.dto.SaveMusicInfoListResponseDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.save.service.SaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/save")
@Tag(name = "/save", description = "데이터 저장 API")
public class SaveController {

    // 서비스
    @Autowired
    private SaveService saveService;

    // API
    // save_001
    @PostMapping("/set/music/info/list/set_music_info_list")
    @Operation(summary = ": 음악 정보 리스트 저장",
            description = "<b>/save/set/music/info/list/set_music_info_list</b><br><br>" +
                    "<b>1. 아이디:</b> <br>" +
                    "- save_001 <br>" +
                    "<br>" +
                    "<b>2. parameter 설명 ( data ):</b> <br>" +
                    "[{" +
                    "   - title : 제목 <br>" +
                    "   - artistName: 가수명 <br>" +
                    "}] <br>" +
                    "<br>"
    )
    public GenericResponseDTO<SaveMusicInfoListResponseDTO> setListMusicInfoSave(
            @RequestBody() SaveMusicInfoListRequestDTO data
    ) {
        // 파라미터 체크
        if (data.getSaveMusicInfoList().isEmpty()) {

            // 빈값
            return GenericResponseDTO.empty();
        }

        // 준비
        SaveMusicInfoListResponseDTO saveMusicInfoListResponseDTO;

        // 시작
        saveMusicInfoListResponseDTO = saveService.setSaveMusicInfoList(data);

        // 완료
        if (saveMusicInfoListResponseDTO == null) {

            // 실패
            return GenericResponseDTO.fail();

        }

        // 성공
        return GenericResponseDTO.success(saveMusicInfoListResponseDTO);
    }

    // save_002
//    @DeleteMapping("/delete/music/info/list/set_music_info_list")
//    @Operation(summary = ": 음악 정보 리스트 저장",
//            description = "<b>/save/set/music/info/list/set_music_info_list</b><br><br>" +
//                    "<b>1. 아이디:</b> <br>" +
//                    "- save_001 <br>" +
//                    "<br>" +
//                    "<b>2. parameter 설명 ( data ):</b> <br>" +
//                    "[{" +
//                    "   - title : 제목 <br>" +
//                    "   - artistName: 가수명 <br>" +
//                    "}] <br>" +
//                    "<br>"
//    )
//    public GenericResponseDTO<SaveMusicInfoListResponseDTO> setListMusicInfoSave(
//            @RequestBody() SaveMusicInfoListRequestDTO data
//    ) {
//        // 파라미터 체크
//        if (data.getSaveMusicInfoList().isEmpty()) {
//
//            // 빈값
//            return GenericResponseDTO.empty();
//        }
//
//        // 준비
//        SaveMusicInfoListResponseDTO saveMusicInfoListResponseDTO;
//
//        // 시작
//        saveMusicInfoListResponseDTO = saveService.setSaveMusicInfoList(data);
//
//        // 완료
//        if (saveMusicInfoListResponseDTO == null) {
//
//            // 실패
//            return GenericResponseDTO.fail();
//
//        }
//
//        // 성공
//        return GenericResponseDTO.success(saveMusicInfoListResponseDTO);
//    }
}
