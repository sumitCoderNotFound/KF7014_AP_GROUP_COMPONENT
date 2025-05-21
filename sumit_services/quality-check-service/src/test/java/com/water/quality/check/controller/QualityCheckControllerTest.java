package com.water.quality.check.controller;

import com.water.quality.check.client.MonitoringServiceClient;
import com.water.quality.check.model.QualityCheckRequest;
import com.water.quality.check.model.QualityCheckResponse;
import com.water.quality.check.security.TokenValidationResponse;
import com.water.quality.check.security.TokenValidator;
import com.water.quality.check.service.QualityCheckService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class QualityCheckControllerTest {

    @Mock
    private QualityCheckService qualityCheckService;

    @Mock
    private MonitoringServiceClient monitoringServiceClient;

    @InjectMocks
    private QualityCheckController qualityCheckController;

    @Mock
    private TokenValidator tokenValidator;


    String fakeAuthHeader = "Bearer faketoken";

    @Test
    void validateWaterQuality_ShouldReturnValidResponse() {
        QualityCheckRequest request = new QualityCheckRequest(7.5, null, 700.0, 3.0, 0.07, null, 0.2, 1.0, 3.5);
        QualityCheckResponse expectedResponse = new QualityCheckResponse("GREEN", 50.0);

        when(monitoringServiceClient.fetchLatestWaterQualityData()).thenReturn(request);
        when(qualityCheckService.validateWaterQuality(request)).thenReturn(expectedResponse);
        when(tokenValidator.validateToken(anyString()))
                .thenReturn(new TokenValidationResponse(true, "testuser", "Token is valid", "ok"));



        ResponseEntity<?> response = qualityCheckController.validateWaterQuality("Bearer faketoken");

        QualityCheckResponse actualResponse = (QualityCheckResponse) response.getBody();

        assertNotNull(actualResponse);
        assertEquals("GREEN", actualResponse.getSafetyFlag());
        assertEquals(50.0, actualResponse.getCalculatedTDS());
    }
    @Test
    void validateWaterQuality_ShouldHandleNullRequest() {
        when(monitoringServiceClient.fetchLatestWaterQualityData()).thenReturn(null);
        when(qualityCheckService.validateWaterQuality(null)).thenReturn(new QualityCheckResponse("RED", null));
        when(tokenValidator.validateToken(anyString()))
                .thenReturn(new TokenValidationResponse(true, "testuser", "Token is valid", "ok"));

        ResponseEntity<?> response = qualityCheckController.validateWaterQuality("Bearer faketoken");

        QualityCheckResponse actualResponse = (QualityCheckResponse) response.getBody();


        assertNotNull(actualResponse);
        assertEquals("RED", actualResponse.getSafetyFlag());
        assertNull(actualResponse.getCalculatedTDS());
    }
}
