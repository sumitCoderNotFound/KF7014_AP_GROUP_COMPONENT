package com.assessment3.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class GatewayApplication {


	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	/**
	 * Configures routing for the microservices a route for the monitoring 
	 * microservice and a route for the water quality microservice with a circuit breaker.
	 * 
	 * @param builder the RouteLocatorBuilder is used to configure routes
	 * @return a RouteLocator object that defines the routing rules for the application
	 * 
	 * @author Prathamesh Belnekar
	 * @version 1.0
	 */
	@Bean 
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()

				// Route for the monitoring microservice
				.route("monitoringService",
						p-> p
						.path("/watermonitoring/**") // If the request matches this path will be forwarded to http://localhost:8081. 
						.uri("http://localhost:8081"))  // The request is forwarded to "http://localhost:8081".


				// Route for the water quality microservice with circuit breaker
				.route("qualityService",
						p -> p
						.path("/waterquality/**")  // Requests matching this path will be forwarded to the Water Quality Microservice.
						.filters(f->f.circuitBreaker(config -> config
								.setName("waterQualityConfig") 
								.setFallbackUri("forward:/fallback"))) // Defines a fallback route in case of failures.

						.uri("http://localhost:8082")) // The request is forwarded to "http://localhost:8082".
				.build(); 
	}
}


