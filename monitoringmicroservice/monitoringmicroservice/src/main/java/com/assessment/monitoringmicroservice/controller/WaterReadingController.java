package com.assessment.monitoringmicroservice.controller;

import java.util.List;
import java.util.Map;

import com.assessment.monitoringmicroservice.model.WaterReading;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import com.assessment.monitoringmicroservice.service.WaterReadingService;



/**
 * This controller reads water quality data from a CSV file which acts as an IOT device to provide water 
 * samples the data will be read continuously with an interval of 30 seconds and store
 * it in the database table, an endpoint is provided to get the latest record from the database table.
 * 
 * @author Prathamesh Belnekar
 * @version 1.0
 */
@RestController
@RequestMapping("/watermonitoring")
public class WaterReadingController {

	private final WaterReadingService waterReadingService;
	private static final Logger logger = LoggerFactory.getLogger(WaterReadingController.class);

	public WaterReadingController(WaterReadingService waterReadingService) {
		this.waterReadingService = waterReadingService;
	}


	/**
	 * Retrieves the latest water reading record from the database based on timestamp.
	 * If no records are found it returns 204 no content response
	 * 
	 * @return {@link ResponseEntity} which has the latest {@link WaterReading} record if its available and
	 * if no records exists 204 No Content is returned.
	 * 
	 */
	@Operation(summary = "Get the latest water reading",
			description = "Retrieves the latest water quality record from the database table.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved the latest record.",
					content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = WaterReading.class))),
			@ApiResponse(responseCode = "204", description = "No records found, response body will be empty"),
			@ApiResponse(responseCode = "500", description = "Internal server error.",
			content = @Content(mediaType = "application/json"))
	})	    
	@GetMapping("records/latest")
	public ResponseEntity<?> getLatestRecord() {
		try {

			WaterReading latestReading = waterReadingService.getLatestRecord();
			return latestReading != null ? ResponseEntity.ok().body(latestReading) 
					: ResponseEntity.noContent().build();

		} catch (DataAccessException de) {
			logger.error("Database error occurred while fetching the latest record.", de.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Database error:" + de.getMessage()));
		} catch (Exception e) {
			logger.error("Server error occurred while fetching the latest record.", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Internal server error:" + e.getMessage()));
		}
	}


	/**
	 * Retrieves all water quality records from the database.
	 *
	 * <p> This method fetches all the water quality records stored in the database.
	 * also handles case where no records were found and manages error which may occur 
	 * due to server or database issue.</p>
	 *
	 * 200 OK - Successfully retrieved the records.
	 * 204 No Content - No records are available in the database.
	 * 500 Internal Server Error - An error occurred while retrieving the records.
	 *
	 * @return A {@link ResponseEntity} containing a list of {@link WaterReading} records if available, 
	 *         or HTTP status codes if no records exist or any errors from server.
	 */
	@Operation(
			summary = "Get all water quality records",
			description = "Fetches all the water quality records from the database table."
			)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved the records.",
					content = @Content(mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = WaterReading.class)))),
			@ApiResponse(responseCode = "204", description = "No records available, response body will be empty"),
			@ApiResponse(responseCode = "500", description = "Internal server error.",
			content = @Content(mediaType = "application/json"))
	})
	@GetMapping("/records")
	public ResponseEntity<?> getAllRecords() {
		try {

			List<WaterReading> records = waterReadingService.getAllRecords();

			// Returns 200 OK if data is retrieved
			// Returns 204 No Content if no records are found
			return records.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(records);

		} catch (DataAccessException de) {
			logger.error("Database error occurred while fetching records.", de.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Database error:" + de.getMessage()));
		
		} catch (Exception e) {
			logger.error("Server error occurred while fetching records.", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Internal server error:" + e.getMessage()));
		}
	}
}






