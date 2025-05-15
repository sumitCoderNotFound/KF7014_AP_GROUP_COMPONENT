package com.assessment2.waterqualitymicroservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.client.ResourceAccessException;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;



public class WaterQualityServiceTests {

	@InjectMocks
    private WaterQualityService waterQualityService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initializing mocks
    }

    @Test
    void testGetLatestWaterQualitySuccess() {
        // Arrange: mock response data
        Map<String, Object> mockData = new HashMap<>();
        mockData.put("cusol1mgl", 0.0019);
        mockData.put("cusol2ugl", 2.76);
        mockData.put("fesol1ugl", 383.29);
        mockData.put("znsolugl", 8.84);
        mockData.put("phph", 7.5);
        mockData.put("alkmgl", 45.0);
        mockData.put("conduscm", 204);
        mockData.put("no2nmgl", 0.024);

        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(mockData, HttpStatus.OK);

        // Mock RestTemplate to return the data
        when(restTemplate.exchange(
                eq("http://localhost:8081/watermonitoring/records/latest"),
                eq(org.springframework.http.HttpMethod.GET),
                isNull(),
                eq(new ParameterizedTypeReference<Map<String, Object>>() {
                	
                })
                )
        ).thenReturn(responseEntity);

        // Act
        ResponseEntity<Map<String, Object>> result = waterQualityService.getLatestWaterQuality();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().containsKey("totalDissolvedSolids"));
        assertTrue(result.getBody().containsKey("safetyFlag"));
    }

    @Test
    void testGetLatestWaterQualityNoContent() {
        // Arrange
        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        when(restTemplate.exchange(
                eq("http://localhost:8081/watermonitoring/records/latest"),
                eq(org.springframework.http.HttpMethod.GET),
                isNull(),
                eq(new ParameterizedTypeReference<Map<String, Object>>() {
                	
                }
                ))
        ).thenReturn(responseEntity);

        // Act
        ResponseEntity<Map<String, Object>> result = waterQualityService.getLatestWaterQuality();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testGetLatestWaterQualityServiceUnavailable() {
        // Arrange
        when(restTemplate.exchange(
                eq("http://localhost:8081/watermonitoring/records/latest"),
                eq(org.springframework.http.HttpMethod.GET),
                isNull(),
                eq(new ParameterizedTypeReference<Map<String, Object>>() {
                	
                }
                ))
        ).thenThrow(new ResourceAccessException("Service timeout"));

        // Act
        ResponseEntity<Map<String, Object>> result = waterQualityService.getLatestWaterQuality();

        // Assert
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, result.getStatusCode());
        assertTrue(result.getBody().containsKey("error"));
    }

    @Test
    void testGetLatestWaterQualityInternalServerError() {
        // Arrange
        when(restTemplate.exchange(
                eq("http://localhost:8081/watermonitoring/records/latest"),
                eq(org.springframework.http.HttpMethod.GET),
                isNull(),
                eq(new ParameterizedTypeReference<Map<String, Object>>() {
                	
                }
                ))
        ).thenThrow(new RuntimeException("Internal error"));

        // Act
        ResponseEntity<Map<String, Object>> result = waterQualityService.getLatestWaterQuality();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertTrue(result.getBody().containsKey("error"));
    }

}
