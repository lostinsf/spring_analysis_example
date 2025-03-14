package kr.spr.analysis.example25001.spring_analysis_example.level4.save
    .controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.SystemMessageEnum;
import kr.spr.analysis.example25001.spring_analysis_example.level3._tool.dto.GenericResponseDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.save.dto.SaveMusicInfoListStoreRequestDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.save.dto.SaveMusicInfoListStoreResponseDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.save.service.SaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/save")
@Tag(name = "/save", description = "데이터 저장 API")
public class SaveController {

    // 서비스
    @Autowired
    private SaveService saveService;

    // API
    // save_001
    @PostMapping("/store/music/info/list/store_music_info_list")
    @Operation(summary = ": 음악 정보 리스트 저장",
        description = "<b>/save/store/music/info/list/store_music_info_list</b" +
            "><br><br>" +
            "<b>1. 아이디:</b> <br>" +
            "- save_001 <br>" +
            "<br>" +
            "<b>2. parameter 설명 ( data ):</b> <br>" +
            "       - musicTitle : 음악제목 <br>" +
            "       - artistName: 가수명 <br>" +
            "<br>"
    )
    public GenericResponseDTO<SaveMusicInfoListStoreResponseDTO> storeListMusicInfoSave(
        @RequestBody SaveMusicInfoListStoreRequestDTO data
    ) {
        // 파라미터 체크
        if (data.getSaveMusicInfoList().isEmpty()) {

            // 빈값
            return GenericResponseDTO.empty();
        }

        // 준비
        SaveMusicInfoListStoreResponseDTO saveMusicInfoListStoreResponseDTO;

        // 시작
        saveMusicInfoListStoreResponseDTO = saveService.storeSaveMusicInfoList(data);

        // 완료
        if (saveMusicInfoListStoreResponseDTO.getStatus() == SystemMessageEnum.FAIL) {

            // 실패
            return GenericResponseDTO.fail();

        }
        // 성공
        return GenericResponseDTO.success(saveMusicInfoListStoreResponseDTO);
    }

    // save_002
    @PutMapping("/delete/music/info/list/delete_music_info_list")
    @Operation(summary = ": 음악 정보 리스트 삭제",
        description =
            "<b>/save/delete/music/info/list/delete_music_info_list" +
                "</b" +
                "><br><br>" +
                "<b>1. 아이디:</b> <br>" +
                "- save_002 <br>" +
                "<br>" +
                "<b>2. parameter 설명 ( data ):</b> <br>" +
                "       - title : 제목 <br>" +
                "       - artistName: 가수명 <br>" +
                "<br>"
    )
    public GenericResponseDTO<SaveMusicInfoListStoreResponseDTO> deleteListMusicInfoSave(
        @RequestBody SaveMusicInfoListStoreRequestDTO data
    ) {
        // 파라미터 체크
        if (data.getSaveMusicInfoList().isEmpty()) {

            // 빈값
            return GenericResponseDTO.empty();
        }

        // 준비
        SaveMusicInfoListStoreResponseDTO saveMusicInfoListStoreResponseDTO;

        // 시작
        saveMusicInfoListStoreResponseDTO =
            saveService.deleteSaveMusicInfoList(data);

        // 완료
        if (saveMusicInfoListStoreResponseDTO.getStatus() == SystemMessageEnum.FAIL) {

            // 실패
            return GenericResponseDTO.fail();

        }

        if (saveMusicInfoListStoreResponseDTO.getStatus() == SystemMessageEnum.EMPTY) {

            // 실패
            return GenericResponseDTO.empty();

        }

        // 성공
        return GenericResponseDTO.success(saveMusicInfoListStoreResponseDTO);
    }

}
