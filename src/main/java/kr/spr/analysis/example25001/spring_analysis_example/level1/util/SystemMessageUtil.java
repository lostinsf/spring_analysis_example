package kr.spr.analysis.example25001.spring_analysis_example.level1.util;

import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.SystemMessageEnum;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SystemMessageUtil {

    // 생성자
    private SystemMessageUtil() {

    }

    // 시스템에 사용할 메세지를 출력하는 메서드
    public static void printSystemMessage(
            SystemMessageEnum systemMessageEnum, String message) {

        LocalDateTime now = LocalDateTime.now();
        String nowString = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(nowString + " >> ["
                + systemMessageEnum + "/" + message + "] " + systemMessageEnum.getDescription());
    }

    // 시스템에 사용할 메세지를 출력하는 메서드 (예외 포함)
    public static void printSystemMessageAndException(
            SystemMessageEnum systemMessageEnum, String message, Exception e
    ) {

        LocalDateTime now = LocalDateTime.now();
        String nowString = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println(nowString + " >> ["
                + systemMessageEnum + "/" + message + "] ");
        e.printStackTrace();
    }

}
