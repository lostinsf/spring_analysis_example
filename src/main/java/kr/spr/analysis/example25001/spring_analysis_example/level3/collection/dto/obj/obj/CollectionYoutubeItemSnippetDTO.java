package kr.spr.analysis.example25001.spring_analysis_example.level3.collection.dto.obj.obj;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CollectionYoutubeItemSnippetDTO {

    @JsonProperty("title")
    private String title;

    @JsonProperty("channelId")
    private String channelId;

    @JsonProperty("channelTitle")
    private String channelTitle;

    @Lob
    @JsonProperty("description")
    private String description;

}
