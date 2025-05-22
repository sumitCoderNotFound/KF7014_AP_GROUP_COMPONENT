package com.water.quality.monitoring.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@TestConfiguration
public class TestConfig {
    
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("test-scheduler-");
        return scheduler;
    }

    @Bean
    public MonitoringConfig monitoringConfig() {
        MonitoringConfig config = new MonitoringConfig();
        config.setCsvFilePath("test.csv");
        config.setCsvDelimiter(",");
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        config.setReadIntervalSeconds(3600);
        return config;
    }
} 