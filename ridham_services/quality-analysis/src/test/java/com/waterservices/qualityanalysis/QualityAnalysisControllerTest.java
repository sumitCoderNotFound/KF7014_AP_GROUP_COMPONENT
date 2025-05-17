package com.waterservices.qualityanalysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waterservices.qualityanalysis.controller.QualityAnalysisController;
import com.waterservices.qualityanalysis.security.TokenValidationResponse;
import com.waterservices.qualityanalysis.security.TokenValidator;
import com.waterservices.qualityanalysis.service.AnalysisResult;
import com.waterservices.qualityanalysis.service.QualityAnalysisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class QualityAnalysisControllerTest {

    @Mock
    private QualityAnalysisService qualityAnalysisService;

    @InjectMocks
    private QualityAnalysisController qualityAnalysisController;

    @Mock
    private TokenValidator tokenValidator;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        mockMvc = MockMvcBuilders.standaloneSetup(qualityAnalysisController).build();

        // Manually inject the mocked TokenValidator into the controller field
        var field = QualityAnalysisController.class.getDeclaredField("tokenValidator");
        field.setAccessible(true);
        field.set(qualityAnalysisController, tokenValidator);
    }


    @Test
    public void testGetLatestAnalysisResult() throws Exception {
        AnalysisResult mockResult = new AnalysisResult();
        mockResult.setSafeWater(false);
        Map<String, String> alerts = new HashMap<>();
        alerts.put("pH", "pH level is too high."); // Fake crisis!
        mockResult.setAlertMessages(alerts);

        when(tokenValidator.validateToken(anyString())).thenReturn(
                new TokenValidationResponse(true, "testuser", "Token is valid", "ok")
        );

        when(qualityAnalysisService.getLatestAnalysisResult()).thenReturn(mockResult);

        mockMvc.perform(get("/api/quality-analysis/records/latest/analysis")
                        .header("Authorization", "Bearer faketoken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(mockResult)));
    }


    @Test
    public void testGetLatestAnalysisResult_notFound() throws Exception {
        when(qualityAnalysisService.getLatestAnalysisResult()).thenReturn(null);
        when(tokenValidator.validateToken(anyString())).thenReturn(
                new TokenValidationResponse(true, "testuser", "Token is valid", "ok")
        );


        mockMvc.perform(get("/api/quality-analysis/records/latest/analysis")
                        .header("Authorization", "Bearer faketoken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetThresholds() throws Exception {
        Map<String, Double> thresholds = new HashMap<>();
        thresholds.put("pHMax", 8.5);
        thresholds.put("pHMin", 6.5);
        thresholds.put("alkalinityMax", 200.0);
        thresholds.put("conductivityMax", 1500.0);
        thresholds.put("nitriteNMax", 0.5);
        thresholds.put("tdsMax", 1000.0);
        thresholds.put("turbidityMax", 5.0);

        when(qualityAnalysisService.getPHMaxThreshold()).thenReturn(8.5);
        when(qualityAnalysisService.getPHMinThreshold()).thenReturn(6.5);
        when(qualityAnalysisService.getAlkalinityMaxThreshold()).thenReturn(200.0);
        when(qualityAnalysisService.getConductivityMaxThreshold()).thenReturn(1500.0);
        when(qualityAnalysisService.getNitriteNMaxThreshold()).thenReturn(0.5);
        when(qualityAnalysisService.getTdsMaxThreshold()).thenReturn(1000.0);
        when(qualityAnalysisService.getTurbidityMaxThreshold()).thenReturn(5.0);
        when(tokenValidator.validateToken(anyString())).thenReturn(
                new TokenValidationResponse(true, "testuser", "Token is valid", "ok")
        );


        mockMvc.perform(get("/api/quality-analysis/thresholds")
                        .header("Authorization", "Bearer faketoken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(thresholds)));
    }
}