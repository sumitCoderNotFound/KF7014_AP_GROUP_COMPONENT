package com.waterservices.monitoring.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class TokenValidator {

    private final String securityServiceValidationURL = "http://localhost:8120/api/authenticate/validate";

    public boolean isTokenValid(String token) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<TokenValidationResponse> response = restTemplate.exchange(
                    securityServiceValidationURL,
                    HttpMethod.GET,
                    request,
                    TokenValidationResponse.class
            );

            return response.getBody() != null && response.getBody().isValid();
        } catch (HttpClientErrorException e) {
            return false;
        }
}
}
