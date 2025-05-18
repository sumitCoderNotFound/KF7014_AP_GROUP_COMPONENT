package com.example.apigatewayapplication.security;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class TokenValidator {

    private final String securityServiceValidationURL =
            "http://localhost:8080/api/authenticate/validate";

    public TokenValidationResponse validateToken(String token) {
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
            return response.getBody();
        } catch (HttpClientErrorException e) {
            TokenValidationResponse invalid = new TokenValidationResponse();
            invalid.setValid(false);
            invalid.setReason("auth-error");
            invalid.setMessage("Failed to validate token: " + e.getStatusCode());
            return invalid;
        }
    }
}
