package com.water.quality.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("monitoring-service", r -> r.path("/api/water-quality/**")
                        .uri("http://localhost:8112"))
                .route("quality-check-service", r -> r.path("/api/quality-check/**")
                        .uri("http://localhost:8113"))
                .build();
    }
}
