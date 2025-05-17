package com.water.quality.monitoring.service;


import com.water.quality.monitoring.repository.WaterMonitorRepository;
import com.water.quality.monitoring.entity.WaterQuality;
import com.water.quality.monitoring.config.MonitoringConfig;
import com.water.quality.monitoring.exception.CsvProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MonitorService {
    private static final Logger logger = LoggerFactory.getLogger(MonitorService.class);
    private static final int EXPECTED_CSV_COLUMNS = 10;
    private BufferedReader reader;
    private String currentLine;

    @Autowired
    private WaterMonitorRepository repository;

    @Autowired
    private MonitoringConfig config;


    @Scheduled(fixedRateString = "#{@monitoringConfig.readIntervalSeconds * 1000}")
    public void readCsvAndStore() {
        try {
            if (reader == null) {
                initializeReader();
            }

            if ((currentLine = reader.readLine()) != null) {
                System.out.println(currentLine);
                processAndStoreLine(currentLine);
            } else {
                logger.info("Reached end of file, resetting reader");
                reader.close();
                reader = null;
            }
        } catch (IOException e) {
            handleIOException(e);
        } catch (Exception e) {
            handleGenericException(e);
        }
    }

    private void initializeReader() throws IOException {
        logger.info("Initializing CSV reader for file: {}", config.getCsvFilePath());
        reader = new BufferedReader(new FileReader(config.getCsvFilePath()));
        // Skip header line
        reader.readLine();
    }

    private void processAndStoreLine(String line) {
        logger.debug("Processing line: {}", line);
        String[] values = line.split(",", -1); // Use -1 to retain empty values
        for(String value : values) {
            System.out.println(value);
        }
        validateCsvLine(values);

        try {
            WaterQuality record = new WaterQuality();
            record.setId(UUID.randomUUID().toString());
            record.setTimestamp(LocalDateTime.now());

            // Parse CSV values, handling empty fields
            record.setObjectId(values[0].trim().isEmpty() ? null : values[0].trim());  // OBJECTID
            record.setPh(parseDoubleOrDefault(values[1], "pH", 0.0));                   // PHpH
            record.setAlkalinity(parseDoubleOrDefault(values[2], "ALK", 0.0));          // ALK_MGL
            record.setConductivity(parseDoubleOrDefault(values[3], "COND", 0.0));       // COND_USCM
            record.setBod(parseDoubleOrDefault(values[4], "BOD", 0.0));                 // BOD_MGL
            record.setNitrite(parseDoubleOrDefault(values[5], "NO2", 0.0));             // NO2_N_MGL
            record.setCusol1(parseDoubleOrDefault(values[6], "CUSOL1", 0.0));           // CUSOL1_MGL
            record.setCusol2(parseDoubleOrDefault(values[7], "CUSOL2", 0.0));           // CUSOL2_UGL
            record.setFesol1(parseDoubleOrDefault(values[8], "FESOL1", 0.0));           // FESOL1_UGL
            record.setZnsol(parseDoubleOrDefault(values[9], "ZNSOL", 0.0));             // ZN_SOL_UGL

            repository.save(record);
            logger.info("Successfully stored record with ID: {}, ObjectID: {}", record.getId(), record.getObjectId());
        } catch (NumberFormatException e) {
            throw new CsvProcessingException("Error parsing numeric value: " + e.getMessage());
        }
    }

    /**
     * Parses a double value, returning a default if empty or invalid.
     */
    private double parseDoubleOrDefault(String value, String fieldName, double defaultValue) {
        try {
            return (value == null || value.trim().isEmpty()) ? defaultValue : Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("Invalid number format for {}: '{}'. Using default: {}", fieldName, value, defaultValue);
            return defaultValue;
        }
    }

    private void validateCsvLine(String[] values) {
        System.out.println("Curr len :"+values.length);
        if (values.length != EXPECTED_CSV_COLUMNS) {
            throw new CsvProcessingException(
                String.format("Invalid CSV line format. Expected %d columns but found %d", 
                    EXPECTED_CSV_COLUMNS, values.length));
        }
    }

    private double parseDouble(String value, String fieldName) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new CsvProcessingException(
                String.format("Error parsing %s value: %s", fieldName, value));
        }
    }

    private void handleIOException(IOException e) {
        logger.error("IO error while reading CSV file", e);
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ignored) {}
            reader = null;
        }
        throw new CsvProcessingException("Error reading CSV file", e);
    }

    private void handleGenericException(Exception e) {
        logger.error("Unexpected error while processing CSV", e);
        throw new CsvProcessingException("Unexpected error while processing CSV", e);
    }
}
