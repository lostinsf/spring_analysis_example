package kr.spr.analysis.example25001.spring_analysis_example.level3._tool.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.SystemMessageEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusResponseDTO {

    @JsonIgnore()
    private SystemMessageEnum status = SystemMessageEnum.FAIL;


}
