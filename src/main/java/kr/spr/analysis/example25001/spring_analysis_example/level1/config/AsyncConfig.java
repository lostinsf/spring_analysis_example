package kr.spr.analysis.example25001.spring_analysis_example.level1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    // 쓰레드 설정

    @Bean(name = "highPriorityTaskExecutor")
    public Executor highPriorityTaskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 성능에 대한 코어 계산
        int cpuCores = Runtime.getRuntime().availableProcessors(); // 논리적 CPU 코어 수
        int threadCount = cpuCores * 2; // I/O 중심 작업을 가정하여 스레드 수 계산

        // 필수 설정
        System.out.println("System cpuCore = " + cpuCores);
        executor.setCorePoolSize(threadCount);          // 최소 스레드 수
        executor.setMaxPoolSize(threadCount * 2);          // 최대 스레드 수
        executor.setQueueCapacity(1000);       // 작업 대기열 크기

        // 네이밍
        executor.setThreadNamePrefix("high-priority-");

        // 유휴 시간 설정 (초 단위)
        executor.setKeepAliveSeconds(60); // 60초 유휴 시 스레드 종료

        executor.initialize(); // 초기화
        return executor;
    }

    @Bean(name = "lowPriorityTaskExecutor")
    public Executor lowPriorityTaskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 필수 설정 (최소)
        executor.setCorePoolSize(1);          // 최소 스레드 수
        executor.setMaxPoolSize(2);          // 최대 스레드 수
        executor.setQueueCapacity(100);       // 작업 대기열 크기

        // 네이밍
        executor.setThreadNamePrefix("low-priority-");

        // 유휴 시간 설정 (초 단위)
        executor.setKeepAliveSeconds(60); // 60초 유휴 시 스레드 종료

        executor.initialize();
        return executor;
    }

}