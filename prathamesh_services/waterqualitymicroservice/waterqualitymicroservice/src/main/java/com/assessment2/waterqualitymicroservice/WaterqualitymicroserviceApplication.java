package com.assessment2.waterqualitymicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication(scanBasePackages = "com.assessment2.waterqualitymicroservice")
public class WaterqualitymicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaterqualitymicroserviceApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
