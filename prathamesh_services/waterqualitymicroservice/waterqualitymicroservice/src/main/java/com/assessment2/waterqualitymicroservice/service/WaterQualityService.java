package com.assessment2.waterqualitymicroservice.service;

import org.springframework.web.client.RestTemplate;

import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Service class for handling water quality calculations.
 * 
 * <p>This service interacts with the water monitoring service to retrieve the latest water quality data,
 * calculates the total dissolved solids and other water quality values and 
 * sets the safety flag based on WHO parameters.</p>
 * 
 * @author Prathamesh Belnekar
 * @version 1.0
 */
@Service
public class WaterQualityService {

	private final RestTemplate restTemplate;
	private static final Logger logger = LoggerFactory.getLogger(WaterQualityService.class); // logger
	private static final String MONITORING_SERVICE_URL = "http://localhost:8081/watermonitoring/records/latest"; // water data url

	/**
	 * Constructs a WaterQualityService with a RestTemplate for external API calls.
	 *
	 * @param restTemplate the RestTemplate used for communicating with the monitoring service.
	 */
	public WaterQualityService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

    /**
     * Fetches the latest water quality record from the monitoring service and processes the data.
     * 
     * <p>This method makes an HTTP GET request to the water monitoring service to retrieve the latest water quality data.
     * and then calculates the total dissolved solids (TDS), evaluates the water safety flag based on the WHO parameters.</p>
     * 
     * @return a ResponseEntity containing the water quality data with TDS and safety flag, or an error message.
     */
	public ResponseEntity<Map<String, Object>> getLatestWaterQuality() {
		try {
			logger.info("Fetching latest water quality data from monitoring service...");


			// Fetch the data from the water monitoring service
			ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
					MONITORING_SERVICE_URL,
					HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {

					});

			// checks if response was successful and valid data is present
			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && !response.getBody().isEmpty()) {
				Map<String, Object> waterReadings = response.getBody();

				// Calculates total dissolved solids
				double totalDissolvedSolids = calculateDisolvedSolids(waterReadings);
				logger.debug("Calculated total Disolved solids: {}", totalDissolvedSolids);

				// Calculates water safety and adds a flag
				String safetyFlag = waterSafetyCheck(waterReadings, totalDissolvedSolids);
				logger.debug("Water safety flag: {}", safetyFlag);

				// Adds total dissolved solids to the response
				waterReadings.put("totalDissolvedSolids", totalDissolvedSolids);

				// Adds a safety flag to the response
				waterReadings.put("safetyFlag", safetyFlag);

				logger.info("Successfully processed water quality data.");
				return ResponseEntity.ok(waterReadings);
			}

			logger.warn("No water quality data found.");
			return ResponseEntity.noContent().build();

		} catch (ResourceAccessException ex) {
			logger.error("Service timeout while fetching water quality data", ex.getMessage());
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
					.body(Map.of("error", "Service timeout", "message", ex.getMessage()));

		} catch(RuntimeException re) {
			logger.error("Other errors fetching water quality data", re.getMessage());
			return ResponseEntity.internalServerError().body(Map.of("error","Internal server error"));

		} catch (Exception e) {
			logger.error("Other errors fetching water quality data", e);
			return ResponseEntity.internalServerError().body(Map.of("error", "failed to fetch water data"));
		}
	}

	
	/**
	 * Calculates the total dissolved solids from the water quality data.
	 * 
	 * <p>Total dissolved solids is the sum of Cusol1, Cusol2, Fesol1, and Znsol </p>
	 * 
	 * @param data the water quality data of all dissolved substances.
	 * @return the calculated total dissolved solids.
	 */
	private double calculateDisolvedSolids(Map<String, Object> data) {

		logger.debug("Calculating total dissolved solids...");

		double totalDissolvedSolids = 0.0;

		if (data.containsKey("cusol1mgl") && data.get("cusol1mgl") != null) {

			totalDissolvedSolids += ((Number) data.get("cusol1mgl")).doubleValue();
		}

		if (data.containsKey("cusol2ugl") && data.get("cusol2ugl") != null) {
			totalDissolvedSolids += convertUglToMgL(((Number) data.get("cusol2ugl")).doubleValue());
		}

		if (data.containsKey("fesol1ugl") && data.get("fesol1ugl") != null) {
			totalDissolvedSolids += convertUglToMgL(((Number) data.get("fesol1ugl")).doubleValue());
		}

		if (data.containsKey("znsolugl") && data.get("znsolugl") != null) {
			totalDissolvedSolids += convertUglToMgL(((Number) data.get("znsolugl")).doubleValue());
		}
		return totalDissolvedSolids;
	}

	/**
	 * Converts the values from micrograms per liter(ug/L) to milligrams per liter(mg/L).
	 * 
	 * @param value is the value to be converted in ug/L.
	 * @return the converted value in mg/L.
	 */
	private double convertUglToMgL(double value) {
		return value / 1000; // Converts ug/L to mg/L
	}

	/**
	 * Checks if the water quality data is safe based on predefined conditions for ph, alkalinity, conductivity,
	 * nitrite and total dissolved solids.
	 * 
	 * @param data has the water quality data.
	 * @param totalDissolvedSolidsValue the calculated total dissolved solids.
	 * @return a string indicating that if the water is safe ("Green") or unsafe ("Red").
	 */
	private String waterSafetyCheck(Map<String, Object> data, double totalDissolvedSolidsValue) {
		Double ph = checkNullValue(data, "phph");
		Double alkalinity = checkNullValue(data, "alkmgl");
		Double conductivity = checkNullValue(data, "conduscm");
		Double nitrite = checkNullValue(data, "no2nmgl");

		if (totalDissolvedSolidsValue > 1000 ||
				(ph != null && (ph < 6.5 || ph > 8.5)) ||
				(alkalinity != null && alkalinity > 500) ||
				(conductivity != null && conductivity > 2000) ||
				(nitrite != null && nitrite >= 1)) {

			return "Red";	
		}
		return "Green";
	}

	/**
	 * Checks if the data contains the required key and returns its value
	 * if not than returns null
	 * 
	 * @param data is the water quality data.
	 * @param key is the key to check in the data parameter.
	 * @return the value associated with the key or null.
	 */
	private Double checkNullValue(Map<String, Object> data, String key) {
		if (!data.containsKey(key) || data.get(key) == null) {
			return null;
		}

		return ((Number) data.get(key)).doubleValue();
	}
}
