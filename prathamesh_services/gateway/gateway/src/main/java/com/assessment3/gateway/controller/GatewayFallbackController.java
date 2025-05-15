package com.assessment3.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class GatewayFallbackController {
	
		@GetMapping
		public Mono<String> fallback() {
			return Mono.just("Water quality microservice is currently not available. Please try again later.");
		
	}
}
