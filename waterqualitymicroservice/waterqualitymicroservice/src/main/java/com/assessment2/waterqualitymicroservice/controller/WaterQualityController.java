package com.assessment2.waterqualitymicroservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assessment2.waterqualitymicroservice.service.WaterQualityService;

import org.springframework.http.ResponseEntity;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Controller for handling the water quality related requests.
 * It provides an endpoint for fetching the latest water quality data,
 * which includes the  total dissolved solids (TDS) and a safety flag is added based on WHO standards.
 * It interacts with the {@link WaterQualityService} to process the data.
 * 
 * @author Prathamesh Belnekar
 * @version 1.0
 */
@RestController
@RequestMapping("/waterquality")
public class WaterQualityController {

	private final WaterQualityService waterQualityService;

	/**
	 * Constructs a WaterQualityController with a WaterQualityService.
	 *
	 * @param waterQualityService the service handling water quality calculations.
	 */
	public WaterQualityController(WaterQualityService waterQualityService) {
		this.waterQualityService = waterQualityService;
	}

    /**
     * Fetches the latest water quality record from the monitoring service, calculates total dissolved solids (TDS),
     * and assigns a safety flag based on predefined water quality parameters.
     * 
     * @return a {@link ResponseEntity} containing the water quality data
     */
	@Operation(
			summary = "Get latest water quality data with safety assessment",
			description = "Fetches the latest water quality record from the monitoring service calculates  and sets a safety flag.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Successfully retrieved the water quality data",
							content = @Content(mediaType = "application/json", schema = @Schema(
									example = """
											{
											  "id": "72399005-00e4-4f72-b8d5-e5b2aebb3cb1",
											  "phph": 7.7,
											  "alkmgl": 95,
											  "conduscm": 270.8,
											  "bodmgl": 2.4,
											  "no2nmgl": 0.011,
											  "cusol1mgl": 0.002,
											  "cusol2ugl": 0.76,
											  "fesol1ugl": 146.5,
											  "znsolugl": 5,
											  "timestamp": "2025-03-16T21:30:31.083Z",
											  "totalDissolvedSolids": 152.26,
											  "safetyFlag": "Green"
											}
											"""
									))
							),
					@ApiResponse(responseCode = "204", description = "No records found response is empty",content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),
					@ApiResponse(responseCode = "500", description = "Internal server error.",
					content = @Content(mediaType = "application/json"))
			}
			)
	@GetMapping("records/latestflagged")
	public ResponseEntity<Map<String, Object>> getlatestWaterQuality() {
		return waterQualityService.getLatestWaterQuality();

	}

}


