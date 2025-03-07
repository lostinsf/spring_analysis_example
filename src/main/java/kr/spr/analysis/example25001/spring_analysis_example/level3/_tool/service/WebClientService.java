package kr.spr.analysis.example25001.spring_analysis_example.level3._tool.service;


import kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration.SystemMessageEnum;
import kr.spr.analysis.example25001.spring_analysis_example.level1.util.SystemMessageUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
public class WebClientService {


    private final WebClient.Builder webClientBuilder;

    public WebClientService() {

        webClientBuilder = WebClient.builder();
    }
    
    private <T> T getWebClientData(Class<T> clazz, String baseUrl,
                                   String requestURL) {

        WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

        long requestTime = System.currentTimeMillis();

        try {

            // 데이터 수집 (최대 10초까지 대기)
            T result =
                    webClient.get().uri(requestURL).retrieve().bodyToMono(clazz).timeout(Duration.ofSeconds(10)).block();

            requestTime = System.currentTimeMillis() - requestTime;

            // 수집시간 5초 이상시 경고 메세지 출력
            if (requestTime > 5000) {

                SystemMessageUtil.printSystemMessage(SystemMessageEnum.WARN,
                        "getWebClientData -> requestDelay" + requestTime +
                                "ms, baseUrl: " + baseUrl + ", requestURL: " + requestURL);
            }

            return result;

        } catch (Exception e) {

            // 예외 발생시 에러 메세지 출력 후 null 발생
            requestTime = System.currentTimeMillis() - requestTime;
            SystemMessageUtil.printSystemMessageAndException(SystemMessageEnum.EXCEPTION,
                    "getWebClientData -> requestDelay" + requestTime + "ms, " +
                            "baseUrl: " + baseUrl + ", requestURL: " + requestURL, e);
        }

        return null;
    }

}
