package kr.spr.analysis.example25001.spring_analysis_example.level3.save.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.spr.analysis.example25001.spring_analysis_example.level3._tool.dto.StatusResponseDTO;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL) // NULL 값인 필드는 제외
@Setter
@Getter
public class SaveMusicInfoListResponseDTO extends StatusResponseDTO {

}
