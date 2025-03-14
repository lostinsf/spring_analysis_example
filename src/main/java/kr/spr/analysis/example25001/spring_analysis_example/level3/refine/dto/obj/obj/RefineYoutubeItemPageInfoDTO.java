package kr.spr.analysis.example25001.spring_analysis_example.level3.refine.dto.obj.obj;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefineYoutubeItemPageInfoDTO {

    @JsonProperty("totalResults")
    private int totalResults;

    @JsonProperty("resultsPerPage")
    private int resultsPerPage;

}
