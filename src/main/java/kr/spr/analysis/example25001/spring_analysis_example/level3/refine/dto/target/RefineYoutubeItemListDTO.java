package kr.spr.analysis.example25001.spring_analysis_example.level3.refine.dto.target;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.spr.analysis.example25001.spring_analysis_example.level3.refine.dto.target.obj.RefineYoutubeItemDTO;
import kr.spr.analysis.example25001.spring_analysis_example.level3.refine.dto.target.obj.RefineYoutubeItemPageInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RefineYoutubeItemListDTO {

    @JsonProperty("pageInfo")
    private RefineYoutubeItemPageInfoDTO pageInfo;

    @JsonProperty("items")
    private List<RefineYoutubeItemDTO> items;

}
