package com.example.waterquality;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WaterqualityApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaterqualityApplication.class, args);
    }

}
