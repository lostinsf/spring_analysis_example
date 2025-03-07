package kr.spr.analysis.example25001.spring_analysis_example.level1.util;

import java.security.SecureRandom;

public class RandomDataUtil {


    // 생성자
    RandomDataUtil() {

    }

    // 랜덤 문자열 생성
    public static String generateRandomString(int length, String characterText) {

        SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {

            sb.append(
                    characterText.charAt(
                            random.nextInt(characterText.length())
                    ));
        }

        return sb.toString();
    }

}
