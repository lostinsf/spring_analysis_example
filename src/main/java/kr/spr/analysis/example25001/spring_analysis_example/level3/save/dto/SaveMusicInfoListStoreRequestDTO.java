package kr.spr.analysis.example25001.spring_analysis_example.level3.save.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.spr.analysis.example25001.spring_analysis_example.level3.save.dto.obj.SaveMusicInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class SaveMusicInfoListStoreRequestDTO {

    @JsonProperty("list")
    private List<SaveMusicInfoDTO> saveMusicInfoList = new ArrayList<>();

}
