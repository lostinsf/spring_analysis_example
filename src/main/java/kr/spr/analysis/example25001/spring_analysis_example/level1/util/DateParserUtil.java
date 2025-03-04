package kr.spr.analysis.example25001.spring_analysis_example.level1.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DateParserUtil {

    // 가능한 날짜 형식 리스트 정의
    private static final List<DateTimeFormatter> DATE_FORMATTERS = new ArrayList<>();

    static {
        DATE_FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DATE_FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        DATE_FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        DATE_FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // 생성자
    private DateParserUtil() {

    }

    // String을 내부 date_formatters에 맞게 되어 있다면, localDateTime으로 반환 (아닐경우 null 반환)
    public static LocalDateTime parseDateTime(String dateTimeStr) {

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {

            try {

                LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, formatter);

                return localDateTime;

            } catch (DateTimeParseException e) {

                // 포맷이 맞지 않으면 다음 포맷으로 넘어감 => 에러 표시 안함
            }
        }

        return null;
    }

}
