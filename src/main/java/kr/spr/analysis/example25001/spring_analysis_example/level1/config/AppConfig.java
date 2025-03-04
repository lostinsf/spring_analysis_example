package kr.spr.analysis.example25001.spring_analysis_example.level1.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class AppConfig {

    // 앱 설정

    @PostConstruct
    public void initTimeZone() {

        // APP 초기화 후 바로 TIMEZONE 설정을 아시아/한국으로 설정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

}
