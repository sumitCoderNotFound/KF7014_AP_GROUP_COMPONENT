package com.apiauth.API.Security.controller;

/**
 * Response DTO for token validation.
 * This class is used to return the result of token validation to other microservices.
 */
public class TokenValidationResponse {
    private boolean valid;
    private String username;
    private String message;

    // Default constructor for JSON serialization
    public TokenValidationResponse() {
    }

    public TokenValidationResponse(boolean valid, String username, String message) {
        this.valid = valid;
        this.username = username;
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}