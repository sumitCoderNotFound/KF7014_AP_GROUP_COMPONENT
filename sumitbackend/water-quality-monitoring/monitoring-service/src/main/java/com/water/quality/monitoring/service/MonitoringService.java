package com.water.quality.monitoring.service;

import com.opencsv.CSVReader;
import com.water.quality.monitoring.model.WaterQualityRecord;
import com.water.quality.monitoring.repository.WaterQualityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MonitoringService {

    private final WaterQualityRepository repository;
    private static final String CSV_FILE_PATH = "data/River_Water_Quality_Monitoring.csv";
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * Fetch all water quality records
     */
    public List<WaterQualityRecord> getAllRecords() {
        return repository.findAll();
    }

    /**
     * Fetch the latest record (needed for QualityCheckService)
     */
    public WaterQualityRecord getLatestRecord() {
        return repository.findTopByOrderByTimestampDesc()
                .orElseThrow(() -> new RuntimeException("No records found"));
    }

    /**
     * Scheduled job that runs every 30 seconds to read the CSV
     */
    @Scheduled(fixedDelay = 30000) // Runs every 30 seconds
    public void readCsvAndScheduleProcessing() {
        System.out.println("[START] Checking for new data at: " + LocalDateTime.now());

        InputStream csvStream = getClass().getClassLoader().getResourceAsStream(CSV_FILE_PATH);
        if (csvStream == null) {
            System.err.println("CSV file not found: " + CSV_FILE_PATH);
            return;
        }

        try (CSVReader reader = new CSVReader(new InputStreamReader(csvStream))) {
            List<String[]> records = reader.readAll();

            // Fetch latest timestamp from DB
            LocalDateTime lastEntryTime = repository.findTopByOrderByTimestampDesc()
                    .map(WaterQualityRecord::getTimestamp)
                    .orElse(LocalDateTime.MIN);

            System.out.println(" Last entry time in DB: " + lastEntryTime);

            int delay = 0;
            for (String[] data : records.subList(1, records.size())) {
                scheduler.schedule(() -> processRecord(data), delay, TimeUnit.SECONDS);
                delay += 30;
            }

        } catch (Exception e) {
            System.err.println(" Error processing CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Processes a single record asynchronously
     */
    private void processRecord(String[] data) {
        try {
            int objectId = Integer.parseInt(data[0]);
            LocalDateTime recordTimestamp = parseTimestampSafe(data, 10);

            System.out.println("Processing record with OBJECTID: " + objectId + " and timestamp: " + recordTimestamp);


            boolean exists;
            if (data.length <= 10 || data[10].trim().isEmpty()) {
                exists = repository.existsByObjectId(objectId);
            } else {
                exists = repository.findByObjectIdAndTimestamp(objectId, recordTimestamp).isPresent();
            }

            if (exists) {
                System.out.println("Duplicate record found! Skipping OBJECTID: " + objectId);
                return;
            }

            WaterQualityRecord record = new WaterQualityRecord(
                    objectId,
                    parseDoubleSafe(data[1], "pH"),
                    parseDoubleSafe(data[2], "Alkalinity"),
                    parseDoubleSafe(data[3], "Conductivity"),
                    parseDoubleSafe(data[4], "BOD"),
                    parseDoubleSafe(data[5], "Nitrite"),
                    parseDoubleSafe(data[6], "Copper Dissolved 1"),
                    parseDoubleSafe(data[7], "Copper Dissolved 2"),
                    parseDoubleSafe(data[8], "Iron Dissolved"),
                    parseDoubleSafe(data[9], "Zinc Dissolved"),
                    recordTimestamp
            );

            repository.save(record);
            System.out.println("Inserted new record with OBJECTID: " + objectId);

        } catch (Exception ex) {
            System.err.println("Skipping invalid record due to parsing error: " + String.join(",", data));
        }
    }

    /**
     * Safe method to parse timestamp
     */
    private LocalDateTime parseTimestampSafe(String[] data, int index) {
        try {
            if (data.length <= index || data[index].trim().isEmpty()) {
                System.err.println("Timestamp missing, using default value.");
                return LocalDateTime.now();
            }
            String timestampStr = data[index].trim();
            return LocalDateTime.parse(timestampStr, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            System.err.println("Error parsing timestamp: " + data[index]);
            return LocalDateTime.now();
        }
    }

    /**
     * Safe method to parse double values
     */
    protected Double parseDoubleSafe(String value, String columnName) {
        try {
            if (value == null || value.trim().isEmpty()) {
                System.err.println("Missing numeric value in Column '" + columnName + "', using NULL.");
                return null;
            }
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing double in Column '" + columnName + "': " + value);
            return null;
        }
    }
}
