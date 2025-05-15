package com.waterservices.monitoring.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.waterservices.monitoring.model.WaterQuality;
import com.waterservices.monitoring.repository.MonitoringRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Long.parseLong;

/**
 * Service for managing water quality monitoring data.
 */
@Service
@Transactional
public class MonitoringService {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringService.class);

    private final MonitoringRepository repository;
    public CSVReader csvReader;
    public String[] csvHeaders;
    public String[] nextCsvRecord;


    // Todo: maybe we can move the path declaration to application.properties
    //  Tried but gives issue with testing, don't have time to find solution
    // @Value("${monitoring.data.source-csv.path}")
    private static final String PARENT_DATA_FOLDER = "./data/River_Water_Quality_Monitoring.csv";

    /**
     * Constructs a new MonitoringService.
     * @param repository The MonitoringRepository.
     */
    public MonitoringService(MonitoringRepository repository) {
        this.repository = repository;
    }

    /**
     * Initializes the service by reading CSV headers and resetting the database.
     */
    @PostConstruct
    public void initService() {
        Path csvPath = Paths.get(PARENT_DATA_FOLDER);
        try {
            FileReader reader = new FileReader(csvPath.toFile());
            csvReader = new CSVReader(reader);
            csvHeaders = csvReader.readNext(); // Read headers once
            if (csvHeaders != null) {
                logger.info("CSV Headers Initialized:");
                for (String header : csvHeaders) {
                    logger.info("[{}]", header);
                }
            } else {
                logger.error("CSV file is empty or headers are missing.");
            }
            nextCsvRecord = csvReader.readNext();
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error initializing CSV reader: " + e.getMessage());
        }
        // Todo: Is this what we are supposed to do each run ?
        // Reset our DB, each run
        repository.deleteAll();
        repository.flush();
    }

    /**
     * Reads and stores the next record from the CSV file based on a schedule.
     */
    @Transactional
    @Scheduled(fixedRate = 30000) // Run every 30 seconds
    public void readAndStoreNextRecord() {
        if (csvReader == null || csvHeaders == null) {
            logger.error("CSV Reader not initialized properly. Skipping scheduled read.");
            return;
        }

        if (nextCsvRecord != null) {
            WaterQuality waterQuality = parseRecord(csvHeaders, nextCsvRecord);
            if (waterQuality != null) {
                waterQuality.setTimestamp(LocalDateTime.now());
                saveRecord(waterQuality);
                logger.info("Stored record with ObjectId: {}", waterQuality);
            }

            try {
                nextCsvRecord = csvReader.readNext(); // Load the next record for next schedule
                if (nextCsvRecord == null) {
                    logger.info("End of CSV file reached.");
                }
            } catch (IOException | CsvValidationException e) {
                logger.error("Error reading next CSV record: {} ", e.getMessage());
            }
        } else {
            logger.error("No more CSV records to process.");
        }
    }

    /**
     * Saves a WaterQuality record to the repository.
     * @param waterQuality The WaterQuality record to save.
     */
    @Transactional
    public void saveRecord(WaterQuality waterQuality) {
        repository.saveAndFlush(waterQuality);
    }

    /**
     * Retrieves the latest WaterQuality record.
     * @return The latest WaterQuality record.
     */
    public WaterQuality getLatestWaterQualityRecord() {
        return repository.findFirstByOrderByObjectIdDesc();
    }

    /**
     * Retrieves all WaterQuality records.
     * @return A list of all WaterQuality records.
     */
    public List<WaterQuality> getAllWaterQualityRecords() {
        return repository.findAll();
    }

    /**
     * Parses a CSV record into a WaterQuality object.
     * @param headers The CSV headers.
     * @param record The CSV record.
     * @return The parsed WaterQuality object.
     */
    public WaterQuality parseRecord(String[] headers, String[] record) {
        if (record == null || record.length != headers.length) {
            return null; // Skip if record is invalid or end of file
        }
        WaterQuality waterQuality = new WaterQuality();
        waterQuality.setObjectId(parseLong(record[getColumnIndex(headers, "OBJECTID")]));
        waterQuality.setpH(parseDouble(record[getColumnIndex(headers, "PHpH")]));
        waterQuality.setAlkalinity(parseDouble(record[getColumnIndex(headers, "ALK_MGL")]));
        waterQuality.setConductivity(parseDouble(record[getColumnIndex(headers, "COND_USCM")]));
        waterQuality.setBod(parseDouble(record[getColumnIndex(headers, "BOD_MGL")]));
        waterQuality.setNitriteN(parseDouble(record[getColumnIndex(headers, "NO2_N_MGL")]));
        waterQuality.setCopperMgL(parseDouble(record[getColumnIndex(headers, "CUSOL1_MGL")]));
        waterQuality.setCopperUgL(parseDouble(record[getColumnIndex(headers, "CUSOL2_UGL")]));
        waterQuality.setIronUgL(parseDouble(record[getColumnIndex(headers, "FESOL1_UGL")]));
        waterQuality.setZincUgL(parseDouble(record[getColumnIndex(headers, "ZN_SOL_UGL")]));
        return waterQuality;
    }

    /**
     * Gets the index of a column in the CSV headers.
     * @param headers The CSV headers.
     * @param headerName The name of the header.
     * @return The index of the column, or -1 if not found.
     */
    private int getColumnIndex(String[] headers, String headerName) {
        for (int i = 0; i < headers.length; i++) {
            if (cleanHeader(headers[i]).equals(headerName)) {
                return i;
            }
        }
        return -1; // Header not found
    }

    /**
     * Parses a string value to a Double.
     * @param value The string value.
     * @return The parsed Double value, or null if parsing fails.
     */
    private Double parseDouble(String value) {
        try {
            return value != null && !value.trim().isEmpty() ? Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Cleans a CSV header string.
     * @param header The header string.
     * @return The cleaned header string.
     */
    private String cleanHeader(String header) {
        if (header == null) return "";
        return header.replace("\uFEFF", "")
                .replaceAll("[^\\x20-\\x7E]", "")
                .trim();
    }
}