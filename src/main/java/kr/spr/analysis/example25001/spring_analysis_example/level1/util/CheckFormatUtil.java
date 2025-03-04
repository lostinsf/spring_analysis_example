package kr.spr.analysis.example25001.spring_analysis_example.level1.util;

import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.CheckFormatEnum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckFormatUtil {

    // 생성자
    private CheckFormatUtil() {

    }

    // CheckFormatEnum에 있는 타잎과 맞는지 체크하는 메서드
    public static boolean isMatchingCheckText(CheckFormatEnum checkFormatEnum,
                                              String checkText) {

        Pattern pattern = Pattern.compile(checkFormatEnum.getDescription());
        Matcher matcher = pattern.matcher(checkText);

        return matcher.matches();
    }

    // 문자가 숫자인지 체크하는 매서드
    public static boolean isNumeric(String str) {

        boolean isTrue = str != null && str.matches("\\d+");

        return isTrue;
    }

}
