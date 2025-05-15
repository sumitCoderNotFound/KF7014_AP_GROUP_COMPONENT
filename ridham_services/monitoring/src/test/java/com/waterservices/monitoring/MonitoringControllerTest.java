package com.waterservices.monitoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waterservices.monitoring.controller.MonitoringController;
import com.waterservices.monitoring.model.WaterQuality;
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

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(monitoringController).build(); // MockMvc sandbox
    }

    /** Tests successful retrieval of all records.
     * @throws Exception if test fails.
     */
    @Test
    public void testGetAllRecordsSuccess() throws Exception {
        List<WaterQuality> records = Arrays.asList(new WaterQuality(), new WaterQuality());

        when(monitoringService.getAllWaterQualityRecords()).thenReturn(records);

        mockMvc.perform(get("/api/monitoring/records"))
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

        mockMvc.perform(get("/api/monitoring/records"))
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

        mockMvc.perform(get("/api/monitoring/records/latest"))
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

        mockMvc.perform(get("/api/monitoring/records/latest"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("")); // No content, empty string is expected
    }
}