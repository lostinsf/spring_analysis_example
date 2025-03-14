package kr.spr.analysis.example25001.spring_analysis_example.level3.refine.dto.obj.obj;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.spr.analysis.example25001.spring_analysis_example.level3.refine.dto.obj.obj.obj.RefineYoutubeItemSnippetResourceIdDTO;

public class RefineYoutubeItemSnippetDTO {

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("resourceId")
    private RefineYoutubeItemSnippetResourceIdDTO resourceId;

}
