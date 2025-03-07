package kr.spr.analysis.example25001.spring_analysis_example.level3._tool.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.SystemMessageEnum;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GenericResponseDTO<T> {

    @JsonProperty("status")
    private String status = "";

    @JsonProperty("message")
    private String message = "";

    @JsonProperty("timestamp")
    private LocalDateTime timestamp = LocalDateTime.now();

    @JsonProperty("data")
    private T data = null;

    // 생성자
    GenericResponseDTO(SystemMessageEnum systemMessageEnum, String message) {

        this.status = systemMessageEnum.toString();

        this.message = message;

    }

    // 생성자 (데이터 포함)
    GenericResponseDTO(SystemMessageEnum systemMessageEnum, String message, T data) {

        this.status = systemMessageEnum.toString();

        this.message = message;

        this.data = data;
    }

    // 매서드
    // 성공 응답 생성 (정적 메서드)
    public static <T> GenericResponseDTO<T> success(T data) {
        return new GenericResponseDTO<>(SystemMessageEnum.SUCCESS, SystemMessageEnum.SUCCESS.getDescription(), data);
    }

    // 빈값 응답 생성 (정적 메서드)
    public static <T> GenericResponseDTO<T> empty() {
        return new GenericResponseDTO<>(SystemMessageEnum.EMPTY, SystemMessageEnum.EMPTY.getDescription());
    }

    // 실패 응답 생성 (정적 메서드)
    public static <T> GenericResponseDTO<T> fail() {
        return new GenericResponseDTO<>(SystemMessageEnum.FAIL, SystemMessageEnum.FAIL.getDescription());
    }

    // 성공 변경 메서드
    public void changeSuccess() {

        this.status = SystemMessageEnum.SUCCESS.toString();

        this.message = SystemMessageEnum.SUCCESS.getDescription();
    }

    // 실패 변경 메서드
    public void changeFail() {

        this.status = SystemMessageEnum.FAIL.toString();

        this.message = SystemMessageEnum.FAIL.getDescription();
    }

    // 메세지 변경 매서드
    public void changeMessage(String message) {

        this.message = message;
    }

    // 시간 변경 매서드
    public void changeTimeStamp(LocalDateTime localDateTime) {

        this.timestamp = localDateTime;
    }

}
