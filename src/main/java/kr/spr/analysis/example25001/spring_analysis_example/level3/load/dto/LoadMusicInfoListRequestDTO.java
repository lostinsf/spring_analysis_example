package kr.spr.analysis.example25001.spring_analysis_example.level3.load.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoadMusicInfoListRequestDTO {

    @JsonProperty("pageNumber")
    private int pageNumber = 0;

    @JsonProperty("pageCount")
    private int pageCount = 0;
}
