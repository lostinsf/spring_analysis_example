package kr.spr.analysis.example25001.spring_analysis_example.level3.refine.dto.target.obj;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.spr.analysis.example25001.spring_analysis_example.level3.refine.dto.target.obj.obj.RefineYoutubeItemSnippetDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefineYoutubeItemDTO {
    
    @JsonProperty("snippet")
    private RefineYoutubeItemSnippetDTO snippet;

}
