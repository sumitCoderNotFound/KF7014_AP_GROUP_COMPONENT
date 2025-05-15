package com.assessment2.waterqualitymicroservice.controller;

import static org.mockito.ArgumentMatchers.any;	
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;



/**
 * Tests for {@link WaterQualityController}.
 * 
 * <p>
 * The test covers Retrieving and processing the water quality data successfully.
 * Handling if the microservice returns an empty response
 * Handling of internal server errors.
 * If there are any service timeouts.
 * </p>
 * 
 * <p>
 * The class uses {@link MockMvc} for simulating HTTP requests and {@link RestTemplate} 
 * for mocking the interactions with the water monitoring service.
 * </p>
 * 
 * @author Prathamesh Belnekar
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
public class WaterQualityControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RestTemplate restTemplate;
	
	
	/**
	 * This test checks whether the water quality data is fetched successfully from the monitoring service and
     * total dissolved solids are calculated and a safety flag is assigned properly based on the data available.
     * 
     * @throws Exception If there is an error during the test.
	 */
	@Test
	void testGetLatestSuccess() throws Exception {

		// Arrange: Sets the mock data
		Map<String, Object> mockData = new HashMap<>();
		mockData.put("objectid", 1115);
		mockData.put("phph", 7.8);
		mockData.put("alkmgl", 65.0);
		mockData.put("conduscm", 500.0);
		mockData.put("bodmgl", 3.0);
		mockData.put("no2nmgl", 0.009);
		mockData.put("cusol1mgl", 0.0012);
		mockData.put("cusol2ugl", 1.49);
		mockData.put("fesol1ugl", 458.88);
		mockData.put("znsolugl", 5.0);

		// Calculate the total dissolved solids
		double tds = mockData.get("cusol1mgl") != null ? ((Number) mockData.get("cusol1mgl")).doubleValue() : 0.0;
		tds += mockData.get("cusol2ugl") != null ? ((Number) mockData.get("cusol2ugl")).doubleValue() / 1000 : 0.0;
		tds += mockData.get("fesol1ugl") != null ? ((Number) mockData.get("fesol1ugl")).doubleValue() / 1000 : 0.0;
		tds += mockData.get("znsolugl") != null ? ((Number) mockData.get("znsolugl")).doubleValue() / 1000 : 0.0;

		mockData.put("totalDissolvedSolids", tds);

		// calculating the safety flag 
		String safetyFlag = (tds > 1000 ||
				(mockData.get("phph") != null && (((Number) mockData.get("phph")).doubleValue() < 6.5 || ((Number) mockData.get("phph")).doubleValue() > 8.5)) ||
				(mockData.get("alkmgl") != null && ((Number) mockData.get("alkmgl")).doubleValue() > 500) ||
				(mockData.get("conduscm") != null && ((Number) mockData.get("conduscm")).doubleValue() > 2000) ||
				(mockData.get("no2nmgl") != null && ((Number) mockData.get("no2nmgl")).doubleValue() >= 1))
				? "Red" : "Green";

		mockData.put("safetyFlag", safetyFlag);

		// Creating a ResponseEntity with the mock data and status is OK
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(mockData, HttpStatus.OK);

		// Mocking the exchange method of RestTemplate
		when(restTemplate.exchange(
				anyString(),
				eq(HttpMethod.GET),
				any(),
				eq(new ParameterizedTypeReference<Map<String, Object>>() {})
				)).thenReturn(responseEntity);

		// Act: sending GET request 
		mockMvc.perform(get("/waterquality/records/latestflagged")) 

		// Assert: verifying the response
		.andExpect(status().isOk()) 
		.andExpect(jsonPath("$.objectid").value(1115))
		.andExpect(jsonPath("$.phph").value(7.8))
		.andExpect(jsonPath("$.alkmgl").value(65.0))
		.andExpect(jsonPath("$.conduscm").value(500.0))
		.andExpect(jsonPath("$.bodmgl").value(3.0))
		.andExpect(jsonPath("$.no2nmgl").value(0.009))
		.andExpect(jsonPath("$.cusol1mgl").value(0.0012))
		.andExpect(jsonPath("$.cusol2ugl").value(1.49))
		.andExpect(jsonPath("$.fesol1ugl").value(458.88))
		.andExpect(jsonPath("$.znsolugl").value(5.0))
		.andExpect(jsonPath("$.totalDissolvedSolids").value(tds))
		.andExpect(jsonPath("$.safetyFlag").value(safetyFlag));
	}	
	
	
	/**
	 * Test case to check if the monitoring service returns no content (204).
	 * 
	 * @throws Exception If there is an error during the execution.
	 */
	@Test
	void testGetNoContent() throws Exception {

		// Arrange
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		when(restTemplate.exchange(
				anyString(),
				any(),
				any(),
				eq(new ParameterizedTypeReference<Map<String, Object>>() {})
				)).thenReturn(responseEntity);

		// Act: Send the GET request.
		mockMvc.perform(get("/waterquality/records/latestflagged"))

		// Assert: Verify if the response is 204 or No Content
		.andExpect(status().isNoContent());
	}
	
	
	
	/**
	 * Test case for server errors (500).
	 * 
	 * @throws Exception If there is an error during the execution.
	 */
	@Test
	void testGetInternalServerError() throws Exception {
		// Arrange
		when(restTemplate.exchange(
				anyString(),
				any(),
				any(),
				eq(new ParameterizedTypeReference<Map<String, Object>>() {})
				)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"Internal server error"));


		// Act: Send the GET request
		mockMvc.perform(get("/waterquality/records/latestflagged"))

		// Assert: Verify if the response is 500
		.andExpect(status().isInternalServerError())
		.andExpect(jsonPath("$.error").value("Internal server error")); 
	}
	
    /**
     * Test case for service timeouts (503).
     * 
     * @throws Exception If there is an error during the test.
     */
	@Test
	void testGetServiceTimeout() throws Exception {

		// Arrange
		when(restTemplate.exchange(
				anyString(),
				any(),
				any(),
				eq(new ParameterizedTypeReference<Map<String, Object>>() {}))
				).thenThrow(new ResourceAccessException("Service timeout"));

		// Act: Send the GET request.
		mockMvc.perform(get("/waterquality/records/latestflagged"))

		// Assert: Verify if the response is 503 Service Unavailable.
		.andExpect(status().isServiceUnavailable()) 
		.andExpect(jsonPath("$.error").value("Service timeout"))
		.andExpect(jsonPath("$.message").value("Service timeout"));
	}

}

