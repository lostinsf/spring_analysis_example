package kr.spr.analysis.example25001.spring_analysis_example.level1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // 웹소켓 설정

    @Bean
    public WebClient.Builder webClientBuilder() {

        // 웹소켓 메모리 사이즈 설정
        return WebClient.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(10 * 1024 * 1024)); // 최대 10MB로 설정
    }
}
