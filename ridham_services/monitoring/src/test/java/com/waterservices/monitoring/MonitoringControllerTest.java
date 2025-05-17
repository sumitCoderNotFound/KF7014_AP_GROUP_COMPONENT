package com.waterservices.monitoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waterservices.monitoring.controller.MonitoringController;
import com.waterservices.monitoring.model.WaterQuality;
import com.waterservices.monitoring.security.TokenValidationResponse;
import com.waterservices.monitoring.security.TokenValidator;
import com.waterservices.monitoring.service.MonitoringService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Unit tests for the {@link MonitoringController}.
 */
@ExtendWith(MockitoExtension.class)
public class MonitoringControllerTest {

    @Mock
    private MonitoringService monitoringService;

    @InjectMocks
    private MonitoringController monitoringController;

    @Mock
    private TokenValidator tokenValidator;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        mockMvc = MockMvcBuilders.standaloneSetup(monitoringController).build(); // MockMvc sandbox

        // Manually inject the mocked TokenValidator into the controller field
        var field = MonitoringController.class.getDeclaredField("tokenValidator");
        field.setAccessible(true);
        field.set(monitoringController, tokenValidator);
    }

    /** Tests successful retrieval of all records.
     * @throws Exception if test fails.
     */
    @Test
    public void testGetAllRecordsSuccess() throws Exception {
        List<WaterQuality> records = Arrays.asList(new WaterQuality(), new WaterQuality());

        when(monitoringService.getAllWaterQualityRecords()).thenReturn(records);
        when(tokenValidator.validateToken(anyString())).thenReturn(
                new TokenValidationResponse(true, "testuser", "Token is valid", "ok")
        );

        mockMvc.perform(get("/api/monitoring/records")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(records)));
    }

    /** Tests scenario with no records.
     * @throws Exception if test fails.
     */
    @Test
    public void testGetAllRecordsNoContent() throws Exception {
        when(monitoringService.getAllWaterQualityRecords()).thenReturn(List.of());
        when(tokenValidator.validateToken(anyString())).thenReturn(
                new TokenValidationResponse(true, "testuser", "Token is valid", "ok")
        );

        mockMvc.perform(get("/api/monitoring/records")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("")); // No content, empty string is expected
    }

    /** Tests successful retrieval of the latest record.
     * @throws Exception if test fails.
     */
    @Test
    public void testGetLatestRecordSuccess() throws Exception {
        WaterQuality record = new WaterQuality();

        when(monitoringService.getLatestWaterQualityRecord()).thenReturn(record);
        when(tokenValidator.validateToken(anyString())).thenReturn(
                new TokenValidationResponse(true, "testuser", "Token is valid", "ok")
        );

        mockMvc.perform(get("/api/monitoring/records/latest")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(record)));
    }

    /** Tests scenario with no latest record.
     * @throws Exception if test fails.
     */
    @Test
    public void testGetLatestRecordNoContent() throws Exception {
        when(monitoringService.getLatestWaterQualityRecord()).thenReturn(null);
        when(tokenValidator.validateToken(anyString())).thenReturn(
                new TokenValidationResponse(true, "testuser", "Token is valid", "ok")
        );

        mockMvc.perform(get("/api/monitoring/records/latest")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("")); // No content, empty string is expected
    }
}