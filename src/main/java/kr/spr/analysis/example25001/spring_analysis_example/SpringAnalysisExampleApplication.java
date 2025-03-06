package kr.spr.analysis.example25001.spring_analysis_example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringAnalysisExampleApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringAnalysisExampleApplication.class, args);
    }

}
