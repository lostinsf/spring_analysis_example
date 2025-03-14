package kr.spr.analysis.example25001.spring_analysis_example.level3.collection.dto.target.obj;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.spr.analysis.example25001.spring_analysis_example.level3.collection.dto.target.obj.obj.CollectionYoutubeItemIdDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.collection.dto.target.obj.obj.CollectionYoutubeItemSnippetDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CollectionYoutubeItemDTO {

    @JsonProperty("id")
    private CollectionYoutubeItemIdDTO id;

    @JsonProperty("snippet")
    private CollectionYoutubeItemSnippetDTO snippet;

}
