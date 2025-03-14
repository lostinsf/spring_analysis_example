package kr.spr.analysis.example25001.spring_analysis_example.level3.save.dto.obj;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SaveMusicInfoDTO {

    @JsonProperty("musicTitle")
    private String musicTitle = "";

    @JsonProperty("artistName")
    private String artistName = "";

}
