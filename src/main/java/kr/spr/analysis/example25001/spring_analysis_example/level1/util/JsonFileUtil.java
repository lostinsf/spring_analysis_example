package kr.spr.analysis.example25001.spring_analysis_example.level1.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonFileUtil {

    // 생성자
    private JsonFileUtil() {

    }

    // Java 객체를 JSON 파일로 저장
    public static void saveJsonToFile(Object object, String filePath) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {

            objectMapper.writeValue(new File(filePath), object);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    // JSON 파일을 Java 객체로 변환하여 불러오기
    public static <T> T loadJsonFromFile(String filePath, Class<T> valueType) {

        ObjectMapper objectMapper = new ObjectMapper();
        T result = null;

        try {

            result = objectMapper.readValue(new File(filePath), valueType);
        } catch (IOException e) {

            e.printStackTrace();
        }

        return result;
    }

    // 운영체제에 맞게 경로 separator 변환
    public static String getPreviewFolderPath(String folderPath) {

        String result = folderPath.replace("/", File.separator);

        return result;
    }

}
