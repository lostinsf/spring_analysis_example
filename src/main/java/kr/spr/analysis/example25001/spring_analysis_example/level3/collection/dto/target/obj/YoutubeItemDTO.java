package kr.spr.analysis.example25001.spring_analysis_example.level3.collection.dto.target.obj;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.spr.analysis.example25001.spring_analysis_example.level3.collection.dto.target.obj.obj.YoutubeItemIdDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.collection.dto.target.obj.obj.YoutubeItemSnippetDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class YoutubeItemDTO {

    @JsonProperty("id")
    private YoutubeItemIdDTO id;

    @JsonProperty("snippet")
    private YoutubeItemSnippetDTO snippet;

}
