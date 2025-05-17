
package com.waterservices.testing.monitoring;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.waterservices.monitoring.model.WaterQuality;
import com.waterservices.monitoring.repository.MonitoringRepository;
import com.waterservices.monitoring.service.MonitoringService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MonitoringServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringServiceTest.class);

    @Mock
    private MonitoringRepository repository;

    @Mock
    private CSVReader csvReader;

    @InjectMocks
    private MonitoringService monitoringService;

    @BeforeEach
    public void setUp() throws Exception {
        monitoringService.initService();
        monitoringService.csvReader = csvReader;
        monitoringService.csvHeaders = new String[]{
                "OBJECTID", "PHpH", "ALK_MGL", "COND_USCM", "BOD_MGL", "NO2_N_MGL",
                "CUSOL1_MGL", "CUSOL2_UGL", "FESOL1_UGL", "ZN_SOL_UGL"};
    }

    @Test
    public void testReadAndStoreNextRecord() throws IOException, CsvValidationException {
        String[] record = {"1115", "7.7", "95", "275", "2.4", "0.011", "0.0022", "0.76", "146.51", "5"};
        monitoringService.nextCsvRecord = record;

        // Mock CSVReader behavior
        when(csvReader.readNext())
                .thenReturn(record)
                .thenReturn(null);

        logger.info("Before execution - nextCsvRecord: {}", Arrays.toString(monitoringService.nextCsvRecord));
        monitoringService.readAndStoreNextRecord();
        logger.info("Another read to trigger End of file.");
        monitoringService.readAndStoreNextRecord();
        logger.info("After execution - nextCsvRecord: {}", Arrays.toString(monitoringService.nextCsvRecord));

        verify(repository, times(2)).saveAndFlush(any(WaterQuality.class));
        assertNull(monitoringService.nextCsvRecord, "nextCsvRecord should be null after reaching the end of CSV.");
    }

    @Test
    public void testSaveRecord() {
        WaterQuality waterQuality = new WaterQuality();
        monitoringService.saveRecord(waterQuality);
        verify(repository, times(1)).saveAndFlush(waterQuality);
    }

    @Test
    public void testGetLatestWaterQualityRecord() {
        WaterQuality expectedRecord = new WaterQuality();
        when(repository.findFirstByOrderByObjectIdDesc()).thenReturn(expectedRecord);
        WaterQuality actualRecord = monitoringService.getLatestWaterQualityRecord();
        assertEquals(expectedRecord, actualRecord);
    }

    @Test
    public void testGetAllWaterQualityRecords() {
        List<WaterQuality> expectedRecords = Arrays.asList(new WaterQuality(), new WaterQuality());
        when(repository.findAll()).thenReturn(expectedRecords);
        List<WaterQuality> actualRecords = monitoringService.getAllWaterQualityRecords();
        assertEquals(expectedRecords, actualRecords);
    }

    @Test
    public void testParseRecordValid() {
        String[] record = {"1115", "7.7", "95", "275", "2.4", "0.011", "0.0022", "0.76", "146.51", "5"};
        WaterQuality waterQuality = monitoringService.parseRecord(monitoringService.csvHeaders, record);
        assertNotNull(waterQuality);
        assertEquals(1115L, waterQuality.getObjectId());
        assertEquals(7.7, waterQuality.getpH());
        assertEquals(95, waterQuality.getAlkalinity());
    }

    @Test
    public void testParseRecordInvalid() {
        String[] record = {"1115", "7.7", "95"}; // Incorrect number of fields
        WaterQuality waterQuality = monitoringService.parseRecord(monitoringService.csvHeaders, record);
        assertNull(waterQuality);
    }

    @Test
    public void testParseRecordNullValues() {
        String[] record = {"1115", null, "95", null, "2.4", "0.011", "0.0022", "0.76", "146.51", "5"};
        WaterQuality waterQuality = monitoringService.parseRecord(monitoringService.csvHeaders, record);
        assertNotNull(waterQuality);
        assertNull(waterQuality.getpH());
        assertNull(waterQuality.getConductivity());
    }
}