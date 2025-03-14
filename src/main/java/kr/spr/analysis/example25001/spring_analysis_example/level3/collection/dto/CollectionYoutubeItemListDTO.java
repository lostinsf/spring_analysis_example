package kr.spr.analysis.example25001.spring_analysis_example.level3.collection.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.ExampleMusicInfoGroupEnum;
import kr.spr.analysis.example25001.spring_analysis_example.level3.collection.dto.obj.CollectionYoutubeItemDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CollectionYoutubeItemListDTO {

    @JsonProperty("group")
    private String groupEnum;

    @JsonProperty("items")
    private List<CollectionYoutubeItemDTO> items;

    public void setGroupEnum(ExampleMusicInfoGroupEnum exampleMusicInfoGroupEnum) {

        groupEnum = exampleMusicInfoGroupEnum.getDescription();
    }

}
