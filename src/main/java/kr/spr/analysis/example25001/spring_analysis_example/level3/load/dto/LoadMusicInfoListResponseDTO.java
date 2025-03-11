package kr.spr.analysis.example25001.spring_analysis_example.level3.load.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.spr.analysis.example25001.spring_analysis_example.level3._tool.dto.StatusResponseDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.load.dto.obj.LoadMusicInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class LoadMusicInfoListResponseDTO extends StatusResponseDTO {

    @JsonProperty("totalPageNum")
    private int totalPageNum = 1;

    @JsonProperty("list")
    private List<LoadMusicInfoDTO> loadMusicInfoList = new ArrayList<>();

}
