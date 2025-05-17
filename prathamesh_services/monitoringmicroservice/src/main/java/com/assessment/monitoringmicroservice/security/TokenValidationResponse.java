package com.assessment.monitoringmicroservice.security;

/**
 * Response DTO for token validation.
 * This class is used to return the result of token validation to other microservices.
 */
public class TokenValidationResponse {
    private boolean valid;
    private String username;
    private String message;
    private String reason; // NEW FIELD

    public TokenValidationResponse() {}

    public TokenValidationResponse(boolean valid, String username, String message, String reason) {
        this.valid = valid;
        this.username = username;
        this.message = message;
        this.reason = reason;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
