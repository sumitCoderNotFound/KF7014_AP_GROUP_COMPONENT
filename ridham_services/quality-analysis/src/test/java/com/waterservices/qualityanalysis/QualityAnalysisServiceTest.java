package com.waterservices.qualityanalysis;

import com.waterservices.qualityanalysis.model.WaterQualityRecord;
import com.waterservices.qualityanalysis.service.AnalysisResult;
import com.waterservices.qualityanalysis.service.QualityAnalysisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QualityAnalysisServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    // TODO: Why was @InjectMocks causing issues here?, even when we have restTemplateBuilder required by its constructor
    private QualityAnalysisService qualityAnalysisService;

    @BeforeEach
    public void setUp() {when(restTemplateBuilder.build()).thenReturn(restTemplate);

        qualityAnalysisService = new QualityAnalysisService(restTemplateBuilder);

        // Set up thresholds using reflection
        ReflectionTestUtils.setField(qualityAnalysisService, "pHMaxThreshold", 8.0);
        ReflectionTestUtils.setField(qualityAnalysisService, "pHMinThreshold", 6.0);
        ReflectionTestUtils.setField(qualityAnalysisService, "alkalinityMaxThreshold", 100.0);
        ReflectionTestUtils.setField(qualityAnalysisService, "conductivityMaxThreshold", 300.0);
        ReflectionTestUtils.setField(qualityAnalysisService, "nitriteNMaxThreshold", 0.1);
        ReflectionTestUtils.setField(qualityAnalysisService, "tdsMaxThreshold", 500.0);
        ReflectionTestUtils.setField(qualityAnalysisService, "turbidityMaxThreshold", 10.0);

        ReflectionTestUtils.setField(qualityAnalysisService, "monitoringServiceUrl", "http://monitoring-service");
        ReflectionTestUtils.setField(qualityAnalysisService, "monitoringFetchLatestEndpoint", "/api/monitoring/water-quality-records/latest");
    }

    @Test
    public void testFetchLatestWaterQualityRecordSuccess() {
        WaterQualityRecord mockRecord = new WaterQualityRecord();
        mockRecord.setObjectId(1L);

        when(restTemplate.getForObject(anyString(), eq(WaterQualityRecord.class))).thenReturn(mockRecord);

        WaterQualityRecord record = qualityAnalysisService.fetchLatestWaterQualityRecord();

        assertNotNull(record);
        assertEquals(1L, record.getObjectId());
    }

    @Test
    public void testFetchLatestWaterQualityRecordFailure() {
        when(restTemplate.getForObject(anyString(), eq(WaterQualityRecord.class)))
                .thenThrow(new RestClientException("Failed"));

        WaterQualityRecord record = qualityAnalysisService.fetchLatestWaterQualityRecord();

        assertNull(record);
    }

    @Test
    public void testAnalyzeRecordAndAlertSafe() {
        WaterQualityRecord record = new WaterQualityRecord();
        record.setObjectId(1L);
        record.setpH(7.0);
        record.setAlkalinity(90.0);
        record.setConductivity(250.0);
        record.setNitriteN(0.05);
        record.setCopperMgL(100.0);
        record.setCopperUgL(100.0);
        record.setIronUgL(100.0);
        record.setZincUgL(100.0);

        AnalysisResult result = qualityAnalysisService.analyzeRecordAndAlert(record);

        assertTrue(result.isSafeWater());
        assertEquals(new HashMap<>(), result.getAlertMessages());

    }

    @Test
    public void testAnalyzeRecordAndAlertUnsafe() {
        WaterQualityRecord record = new WaterQualityRecord();
        record.setObjectId(1L);
        record.setpH(9.0); // Exceeding pHMaxThreshold

        AnalysisResult result = qualityAnalysisService.analyzeRecordAndAlert(record);

        assertFalse(result.isSafeWater());
        assertNotNull(result.getAlertMessages());
        assertTrue(result.getAlertMessages().containsKey("pH"));
    }

    @Test
    public void testCalculateTDS() {
        WaterQualityRecord record = new WaterQualityRecord();
        record.setCopperMgL(100.0);
        record.setCopperUgL(100.0);
        record.setIronUgL(100.0);
        record.setZincUgL(100.0);

        Double tds = qualityAnalysisService.calculateTDS(record);

        assertEquals(100.3, tds, 0.001);
    }

    @Test
    public void testCalculateTDSNull() {
        WaterQualityRecord record = new WaterQualityRecord();
        record.setCopperMgL(100.0);
        record.setCopperUgL(100.0);
        record.setIronUgL(null);
        record.setZincUgL(100.0);

        Double tds = qualityAnalysisService.calculateTDS(record);

        assertNull(tds);
    }
}