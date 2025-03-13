package kr.spr.analysis.example25001.spring_analysis_example.level3.load.dto.obj;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoadMusicInfoDTO {

    @JsonProperty("musicTitle")
    private String musicTitle = "";

    @JsonProperty("artistName")
    private String artistName = "";

}
