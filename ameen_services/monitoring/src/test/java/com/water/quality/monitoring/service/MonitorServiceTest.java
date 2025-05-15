package com.water.quality.monitoring.service;

import com.water.quality.monitoring.repository.WaterMonitorRepository;
import com.water.quality.monitoring.entity.WaterQuality;
import com.water.quality.monitoring.config.MonitoringConfig;
import com.water.quality.monitoring.exception.CsvProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonitorServiceTest {

    @Mock
    private WaterMonitorRepository repository;

    @Mock
    private MonitoringConfig config;

    @InjectMocks
    private MonitorService monitorService;

    private File tempCsvFile;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary CSV file for testing
        tempCsvFile = Files.createTempFile("test_water_quality", ".csv").toFile();
        tempCsvFile.deleteOnExit();
    }

    @Test
    void whenValidCsvLine_thenProcessSuccessfully() throws IOException {
        // Arrange
        try (FileWriter writer = new FileWriter(tempCsvFile)) {
            writer.write("OBJECTID,PHpH,ALK_MGL,COND_USCM,BOD_MGL,NO2_N_MGL,CUSOL1_MGL,CUSOL2_UGL,FESOL1_UGL,ZN_SOL_UGL\n");
            writer.write("OBJ1,7.5,300,1500,2.5,0.5,0.1,50,100,30\n");
        }
        when(config.getCsvFilePath()).thenReturn(tempCsvFile.getAbsolutePath());
        when(config.getCsvDelimiter()).thenReturn(",");
        when(repository.save(any(WaterQuality.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        monitorService.readCsvAndStore();

        // Assert
        verify(repository, times(1)).save(argThat(record -> {
            assertEquals("OBJ1", record.getObjectId());
            assertEquals(7.5, record.getPh());
            assertEquals(300.0, record.getAlkalinity());
            assertEquals(1500.0, record.getConductivity());
            assertEquals(2.5, record.getBod());
            assertEquals(0.5, record.getNitrite());
            assertEquals(0.1, record.getCusol1());
            assertEquals(50.0, record.getCusol2());
            assertEquals(100.0, record.getFesol1());
            assertEquals(30.0, record.getZnsol());
            assertNotNull(record.getId());
            assertNotNull(record.getTimestamp());
            return true;
        }));
    }

    @Test
    void whenInvalidCsvFormat_thenThrowException() throws IOException {
        // Arrange
        try (FileWriter writer = new FileWriter(tempCsvFile)) {
            writer.write("OBJECTID,PHpH\n");  // Invalid number of columns
            writer.write("OBJ1,7.5\n");
        }
        when(config.getCsvFilePath()).thenReturn(tempCsvFile.getAbsolutePath());
        when(config.getCsvDelimiter()).thenReturn(",");

        // Act & Assert
        assertThrows(CsvProcessingException.class, () -> monitorService.readCsvAndStore());
        verify(repository, never()).save(any());
    }

    @Test
    void whenInvalidNumericValue_thenThrowException() throws IOException {
        // Arrange
        try (FileWriter writer = new FileWriter(tempCsvFile)) {
            writer.write("OBJECTID,PHpH,ALK_MGL,COND_USCM,BOD_MGL,NO2_N_MGL,CUSOL1_MGL,CUSOL2_UGL,FESOL1_UGL,ZN_SOL_UGL\n");
            writer.write("OBJ1,invalid,300,1500,2.5,0.5,0.1,50,100,30\n");  // Invalid pH value
        }
        when(config.getCsvFilePath()).thenReturn(tempCsvFile.getAbsolutePath());
        when(config.getCsvDelimiter()).thenReturn(",");

        // Act & Assert
        assertThrows(CsvProcessingException.class, () -> monitorService.readCsvAndStore());
        verify(repository, never()).save(any());
    }

    @Test
    void whenFileEnds_thenResetReader() throws IOException {
        // Arrange
        try (FileWriter writer = new FileWriter(tempCsvFile)) {
            writer.write("OBJECTID,PHpH,ALK_MGL,COND_USCM,BOD_MGL,NO2_N_MGL,CUSOL1_MGL,CUSOL2_UGL,FESOL1_UGL,ZN_SOL_UGL\n");
            // Empty file after header
        }
        when(config.getCsvFilePath()).thenReturn(tempCsvFile.getAbsolutePath());

        // Act
        monitorService.readCsvAndStore();

        // Assert
        assertNull(ReflectionTestUtils.getField(monitorService, "reader"));
    }

    @Test
    void whenIOException_thenHandleGracefully() {
        // Arrange
        when(config.getCsvFilePath()).thenReturn("nonexistent.csv");

        // Act & Assert
        assertThrows(CsvProcessingException.class, () -> monitorService.readCsvAndStore());
        verify(repository, never()).save(any());
    }
} 