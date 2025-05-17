package com.water.quality.monitoring.service;

import com.water.quality.monitoring.model.WaterQualityRecord;
import com.water.quality.monitoring.repository.WaterQualityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MonitoringServiceTest {

    @Mock
    private WaterQualityRepository repository;

    @InjectMocks
    private MonitoringService monitoringService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private LocalDateTime invokeParseTimestampSafe(String[] data, int index) throws Exception {
        Method method = MonitoringService.class.getDeclaredMethod("parseTimestampSafe", String[].class, int.class);
        method.setAccessible(true);
        return (LocalDateTime) method.invoke(monitoringService, data, index);
    }

    @Test
    void testParseDoubleSafe_ValidValue() {
        Double result = monitoringService.parseDoubleSafe("7.5", "pH");
        assertEquals(7.5, result);
    }

    @Test
    void testParseDoubleSafe_EmptyValue() {
        Double result = monitoringService.parseDoubleSafe("", "Alkalinity");
        assertNull(result, "Parsing empty string should return null.");
    }

    @Test
    void testParseDoubleSafe_NullValue() {
        Double result = monitoringService.parseDoubleSafe(null, "Conductivity");
        assertNull(result, "Parsing null should return null.");
    }

    @Test
    void testParseDoubleSafe_InvalidValue() {
        Double result = monitoringService.parseDoubleSafe("abc", "BOD");
        assertNull(result, "Parsing invalid double should return null.");
    }

    @Test
    void testGetAllRecords_Success() {
        List<WaterQualityRecord> mockRecords = List.of(
                new WaterQualityRecord(101, 7.5, 100.0, 250.0, 3.2, 1.2, 0.1, 0.2, 0.05, 0.03, LocalDateTime.now()),
                new WaterQualityRecord(102, 6.8, 90.0, 240.0, 3.0, 1.1, 0.2, 0.3, 0.04, 0.02, LocalDateTime.now())
        );

        when(repository.findAll()).thenReturn(mockRecords);

        List<WaterQualityRecord> result = monitoringService.getAllRecords();
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testReadCsvAndScheduleProcessing_NewRecord() {
        WaterQualityRecord record = new WaterQualityRecord(202, 7.5, 100.0, 250.0, 3.2, 1.2, 0.1, 0.2, 0.05, 0.03, LocalDateTime.now());

        when(repository.existsByObjectId(202)).thenReturn(false);
        when(repository.save(any(WaterQualityRecord.class))).thenReturn(record);

        monitoringService.readCsvAndScheduleProcessing();

        verify(repository, atLeastOnce()).save(any(WaterQualityRecord.class));
    }
}
