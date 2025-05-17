package com.water.quality.monitoring.controller;

import com.water.quality.monitoring.model.WaterQualityRecord;
import com.water.quality.monitoring.service.MonitoringService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MonitoringControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MonitoringService monitoringService;

    @InjectMocks
    private MonitoringController monitoringController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(monitoringController).build();
    }

    @Test
    void testGetAllRecords_Success() throws Exception {
        List<WaterQualityRecord> mockRecords = List.of(
                new WaterQualityRecord(101, 7.5, 100.0, 250.0, 3.2, 1.2, 0.1, 0.2, 0.05, 0.03, LocalDateTime.now()),
                new WaterQualityRecord(102, 6.8, 90.0, 240.0, 3.0, 1.1, 0.2, 0.3, 0.04, 0.02, LocalDateTime.now())
        );

        when(monitoringService.getAllRecords()).thenReturn(mockRecords);

        System.out.println("Mock response: " + monitoringService.getAllRecords());

        mockMvc.perform(get("/api/water-quality/records")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].pH").value(7.5))
                .andExpect(jsonPath("$[1].pH").value(6.8));
    }
}
