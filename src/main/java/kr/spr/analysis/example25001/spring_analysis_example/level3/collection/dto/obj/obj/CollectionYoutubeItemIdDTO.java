package kr.spr.analysis.example25001.spring_analysis_example.level3.collection.dto.obj.obj;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CollectionYoutubeItemIdDTO {


    @JsonProperty("playlistId")
    private String playlistId;

}
