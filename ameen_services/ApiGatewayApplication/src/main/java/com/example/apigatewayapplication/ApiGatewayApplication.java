package com.example.apigatewayapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Routes for monitoring service
                .route("monitoring_service", r -> r
                        .path("/api/monitoring/**")
                        .uri("http://localhost:8082")) // Redirects to MonitoringApplication

                // Routes for water quality service
                .route("quality_service", r -> r
                        .path("/api/quality/**")
                        .uri("http://localhost:8081")) // Redirects to WaterQualityApplication

                .build();
    }
}

