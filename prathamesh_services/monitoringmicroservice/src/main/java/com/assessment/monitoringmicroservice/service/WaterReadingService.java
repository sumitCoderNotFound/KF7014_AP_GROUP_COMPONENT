package com.assessment.monitoringmicroservice.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import com.assessment.monitoringmicroservice.model.WaterReading;
import com.assessment.monitoringmicroservice.repository.WaterReadingRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Service class for managing water quality readings.
 * 
 * <p>
 * This service provides methods to retrieve water quality data which includes fetching
 * the latest record and retrieving all stored records.
 * </p>
 * 
 * <p>
 * It interacts with {@link WaterReadingRepository} to fetch data from the database.
 * </p>
 * 
 * @author Prathamesh Belnekar
 * @version 1.0
 */
@Service
public class WaterReadingService {

	private final WaterReadingRepository waterReadingRepository;
	private static final String FILE_NAME = "data/River_Water_Quality_Monitoring.csv";  // CSV file stored in the resource folder of the microservice
	private static final Logger logger = LoggerFactory.getLogger(WaterReadingService.class); //logger


	/**
	 * Constructor for WaterReadingController.
	 *
	 * @param waterReadingRepository Repository to save and retrieve the readings.
	 */
	public WaterReadingService(WaterReadingRepository waterReadingRepository) {
		this.waterReadingRepository = waterReadingRepository;
	}


	/**
	 * Initializes the controller and starts the task asynchronously of processing the CSV file
	 * once the application starts.
	 */
	@PostConstruct
	public void init() {
		logger.info("Starting CSV file processing...");
		CompletableFuture.runAsync(this::readFile);
	}


	/**
	 * Reads and processes the CSV file which has water quality data asynchronously.
	 * This method is called after the application starts to load data.
	 */
	@Async
	public void readFile() {
		try {
			ClassPathResource csvFile = new ClassPathResource(FILE_NAME);
			logger.info("Processing file: {}", FILE_NAME);
			processFile(csvFile);
		} catch (Exception e) {
			logger.error("CSV file not found: {}", FILE_NAME, e);
		}
	}


	/**
	 * Processes the CSV file, extracts water quality data, and stores the records in the database.
	 *
	 * @param csvFile is the CSV file which contains the water quality data
	 */  
	public void processFile(ClassPathResource csvFile) {
		List<WaterReading> allParsedRecords = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))) {

			String row;
			reader.readLine(); // Skip the header 

			while ((row = reader.readLine()) != null) { // while the row is not null
				try {
					String[] rowData = row.split(",");
					while (rowData.length < 10) {
						rowData = Arrays.copyOf(rowData, 10); // Ensures there are 10 columns the row.
					}
					WaterReading readingRow = new WaterReading();
					readingRow.setObjectId(parseInteger(rowData[0]));
					readingRow.setPhph(parseDouble(rowData[1]));
					readingRow.setAlkmgl(parseDouble(rowData[2]));
					readingRow.setConduscm(parseDouble(rowData[3]));
					readingRow.setBodmgl(parseDouble(rowData[4]));
					readingRow.setNo2nmgl(parseDouble(rowData[5]));
					readingRow.setCusol1mgl(parseDouble(rowData[6]));
					readingRow.setCusol2ugl(parseDouble(rowData[7]));
					readingRow.setFesol1ugl(parseDouble(rowData[8]));
					readingRow.setZnsolugl(parseDouble(rowData[9]));

					allParsedRecords.add(readingRow); // Adds each record to the arrayList

				} catch (Exception e) { 
					logger.warn("invalid row: {}.Error: {}", row, e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error("Error processing the file: {}", FILE_NAME, e);
		} 
		insertRecords(allParsedRecords);  // Insert the parsed records in the database
	}


	/**
	 * Inserts records into the database table with a delay of 30 seconds between each record.
	 * also sets the current timestamp for each record before inserting 
	 *
	 * @param records ArrayList of WaterReading records to be saved.
	 */
	@Async
	public void insertRecords(List<WaterReading> records) {
		for (WaterReading reading : records) {
			try {
				if (reading == null || reading.getObjectId() == null) {
					logger.warn("Skipping null or invalid record.");
					continue;
				}
				reading.setTimestamp(LocalDateTime.now()); // Set current timestamp before saving the record

				logger.info("Inserting record: {}", reading.getObjectId());

				waterReadingRepository.saveAndFlush(reading); // Save the record immediately.

				TimeUnit.SECONDS.sleep(30);   // Delay of 30 seconds between records

			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
				logger.error("Process stopped inserting record: {}", reading.getObjectId(), ie);
			} catch (Exception e) {
				logger.error("Error while adding records {}: {}", reading.getObjectId(), e.getMessage(), e);
			}
		}
	}


	/**
	 * Parses an integer value from a string or returns a null if parsing fails.
	 *
	 * @param value is the string value to parse.
	 * @return  parsed integer value or null if the value is empty or null or any error occurs.
	 */
	private Integer parseInteger(String value) {
		try {
			return (value == null || value.trim().isEmpty()) ? null : Integer.parseInt(value.trim());
		} catch (NumberFormatException e) {
			logger.warn("Invalid value: {}", value);
			return null;
		}
	}


	/**
	 * parses a double value from a string or returns a null if parsing fails.
	 *
	 * @param value is The string value to parse.
	 * @return parsed double value or null if the value is empty or null or any error occurs.
	 */
	private Double parseDouble(String value) {
		try {
			return (value == null || value.trim().isEmpty()) ? null : Double.parseDouble(value.trim());
		} catch (NumberFormatException e) {
			logger.warn("Invalid value: {}", value);
			return null;
		}
	}


	/**
	 * Retrieves the most recent water quality record from the database.
	 *
	 * @return the latest {@link WaterReading} record if available or {@code null} if no records exist.
	 */
	public WaterReading getLatestRecord() {
		return waterReadingRepository.findTopByOrderByTimestampDesc();
	}


	/**
	 * Retrieves all water quality records from the database.
	 *
	 * @return a list of {@link WaterReading} records or returns an empty list if there are no records.
	 */
	public List<WaterReading> getAllRecords() {
		return Optional.ofNullable(waterReadingRepository.findAll()).orElse(Collections.emptyList());
	}



}