package kr.spr.analysis.example25001.spring_analysis_example.level3.collection.dto.target;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.spr.analysis.example25001.spring_analysis_example.level3.collection.dto.target.obj.YoutubeItemDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CollectionYoutubeItemDTO {

    @JsonProperty("items")
    private List<YoutubeItemDTO> items;

}
