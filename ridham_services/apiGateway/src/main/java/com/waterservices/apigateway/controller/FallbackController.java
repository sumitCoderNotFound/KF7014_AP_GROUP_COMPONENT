package com.waterservices.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Fallback controller to handle scenarios when backend services are unavailable.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {


    /**
     * Fallback method for the Quality Analysis service when it's unavailable (GET request).
     *
     * @return Mono with ResponseEntity containing a message and status indicating the service is down.
     */
    @GetMapping("/quality-analysis")
    public Mono<ResponseEntity<Map<String, String>>> qualityAnalysisFallback() {
        Map<String, String> fallbackResponse = new HashMap<>();
        fallbackResponse.put("message", "Quality Analysis service is currently unavailable.");
        fallbackResponse.put("status", "DOWN");
        return Mono.just(new ResponseEntity<>(fallbackResponse, HttpStatus.SERVICE_UNAVAILABLE));
    }

    /**
     * Fallback method for the Quality Analysis service when it's unavailable (POST request).
     *
     * @return Mono with ResponseEntity containing a message and status indicating the service is down.
     */
    @PostMapping("/quality-analysis")
    public Mono<ResponseEntity<Map<String, String>>> qualityAnalysisFallbackPost() {
        return qualityAnalysisFallback();
    }

    /**
     * Fallback method for the Monitoring service when it's unavailable (GET request).
     *
     * @return Mono with ResponseEntity containing a message and status indicating the service is down.
     */
    @GetMapping("/monitoring")
    public Mono<ResponseEntity<Map<String, String>>> monitoringFallback() {
        Map<String, String> fallbackResponse = new HashMap<>();
        fallbackResponse.put("message", "Monitoring service is currently unavailable.");
        fallbackResponse.put("status", "DOWN");
        return Mono.just(new ResponseEntity<>(fallbackResponse, HttpStatus.SERVICE_UNAVAILABLE));
    }

    /**
     * Fallback method for the Monitoring service when it's unavailable (POST request).
     *
     * @return Mono with ResponseEntity containing a message and status indicating the service is down.
     */
    @PostMapping("/monitoring")
    public Mono<ResponseEntity<Map<String, String>>> monitoringFallbackPost() {
        return monitoringFallback();
    }
}