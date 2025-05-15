package com.assessment.monitoringmicroservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.assessment.monitoringmicroservice.model.WaterReading;
import com.assessment.monitoringmicroservice.repository.WaterReadingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WaterReadingServiceTest {

    @Mock
    private WaterReadingRepository waterReadingRepository;

    @InjectMocks
    private WaterReadingService waterReadingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetLatestReadingSuccess() {
        WaterReading waterReading = new WaterReading();
        waterReading.setObjectId(1556);
        waterReading.setPhph(7.8);
        waterReading.setAlkmgl(65.0);
        waterReading.setConduscm(500.0);
        waterReading.setBodmgl(3.0);
        waterReading.setNo2nmgl(0.009);
        waterReading.setCusol1mgl(0.0012);
        waterReading.setCusol2ugl(1.49);
        waterReading.setFesol1ugl(458.88);
        waterReading.setZnsolugl(5.0);
        waterReading.setTimestamp(LocalDateTime.now());

        when(waterReadingRepository.findTopByOrderByTimestampDesc()).thenReturn(waterReading);

        WaterReading result = waterReadingService.getLatestRecord();
        assertNotNull(result);
        assertEquals(1556, result.getObjectId());
    }

    @Test
    void testGetLatestReadingNoData() {
        when(waterReadingRepository.findTopByOrderByTimestampDesc()).thenReturn(null);

        WaterReading result = waterReadingService.getLatestRecord();
        assertNull(result);
    }

    @Test
    void testGetAllReadingsSuccess() {
        WaterReading reading1 = new WaterReading();
        reading1.setObjectId(1115);
        reading1.setPhph(7.7);
        reading1.setAlkmgl(95.0);
        reading1.setConduscm(300.5);
        reading1.setBodmgl(275.0);
        reading1.setNo2nmgl(0.011);
        reading1.setCusol1mgl(0.02);
        reading1.setCusol2ugl(1.5);
        reading1.setFesol1ugl(0.8);
        reading1.setZnsolugl(2.2);
        reading1.setTimestamp(LocalDateTime.now());

        WaterReading reading2 = new WaterReading();
        reading2.setObjectId(1147);
        reading2.setPhph(6.8);
        reading2.setAlkmgl(68.0);
        reading2.setConduscm(225.0);
        reading2.setBodmgl(4.0);
        reading2.setNo2nmgl(0.005);
        reading2.setCusol1mgl(0.022);
        reading2.setCusol2ugl(2.32);
        reading2.setFesol1ugl(720.88);
        reading2.setZnsolugl(5.0);
        reading2.setTimestamp(LocalDateTime.now());

        List<WaterReading> mockRecords = Arrays.asList(reading1, reading2);
        when(waterReadingRepository.findAll()).thenReturn(mockRecords);

        List<WaterReading> result = waterReadingService.getAllRecords();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllReadingsNoData() {
        when(waterReadingRepository.findAll()).thenReturn(Collections.emptyList());

        List<WaterReading> result = waterReadingService.getAllRecords();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
