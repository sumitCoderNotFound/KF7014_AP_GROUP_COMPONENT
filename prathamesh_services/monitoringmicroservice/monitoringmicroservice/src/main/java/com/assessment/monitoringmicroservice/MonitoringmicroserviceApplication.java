package com.assessment.monitoringmicroservice;

import org.springframework.boot.SpringApplication;	
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync    //enables async for the application
public class MonitoringmicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitoringmicroserviceApplication.class, args);
	}

}
