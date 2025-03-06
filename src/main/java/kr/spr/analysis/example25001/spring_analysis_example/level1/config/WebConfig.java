package kr.spr.analysis.example25001.spring_analysis_example.level1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 웹 설정

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        // CORS 처리 (임시로 모두가 접근 가능하도록 설정)
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE").allowedHeaders("*");
    }

}
