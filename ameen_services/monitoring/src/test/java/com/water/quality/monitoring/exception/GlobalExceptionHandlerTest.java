package com.water.quality.monitoring.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void whenCsvProcessingException_thenReturnInternalServerError() {
        // Arrange
        String errorMessage = "Error processing CSV file";
        CsvProcessingException exception = new CsvProcessingException(errorMessage);

        // Act
        ResponseEntity<Object> response = exceptionHandler.handleCsvProcessingException(exception);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(errorMessage, responseBody.get("message"));
        assertEquals("CSV_PROCESSING_ERROR", responseBody.get("type"));
        assertNotNull(responseBody.get("timestamp"));
    }

    @Test
    void whenCsvProcessingExceptionWithCause_thenReturnInternalServerError() {
        // Arrange
        String errorMessage = "Error processing CSV file";
        Exception cause = new RuntimeException("Root cause");
        CsvProcessingException exception = new CsvProcessingException(errorMessage, cause);

        // Act
        ResponseEntity<Object> response = exceptionHandler.handleCsvProcessingException(exception);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(errorMessage, responseBody.get("message"));
        assertEquals("CSV_PROCESSING_ERROR", responseBody.get("type"));
        assertNotNull(responseBody.get("timestamp"));
    }

    @Test
    void whenGenericException_thenReturnInternalServerError() {
        // Arrange
        Exception exception = new RuntimeException("Unexpected error");

        // Act
        ResponseEntity<Object> response = exceptionHandler.handleGenericException(exception);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals("An unexpected error occurred", responseBody.get("message"));
        assertEquals("INTERNAL_SERVER_ERROR", responseBody.get("type"));
        assertNotNull(responseBody.get("timestamp"));
    }
} 