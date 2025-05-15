package com.example.waterquality.config;

import com.example.waterquality.service.WaterQualityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Application configuration class that provides beans for the Spring application context.
 * Contains configuration for HTTP client and other application-wide components.
 */
@Configuration
public class AppConfig {
    /**
     * Creates and configures a RestTemplate bean for making HTTP requests.
     * This template is used for communicating with the water monitoring service.
     * @return Configured RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WaterQualityService waterQualityService(){
        RestTemplate restTemplate = new RestTemplate();
        return new WaterQualityService(restTemplate);
    }
}
