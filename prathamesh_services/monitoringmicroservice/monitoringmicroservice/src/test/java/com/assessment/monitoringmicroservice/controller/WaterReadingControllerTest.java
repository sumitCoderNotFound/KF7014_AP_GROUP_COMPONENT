package com.assessment.monitoringmicroservice.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.assessment.monitoringmicroservice.model.WaterReading;
import com.assessment.monitoringmicroservice.service.WaterReadingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
public class WaterReadingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WaterReadingService waterReadingService;
    
	@Test
	void testGetLatestSuccess() throws Exception {
		
		// Arrange: Create a sample WaterReading object
		WaterReading reading = new WaterReading();
		reading.setObjectId(1556);
		reading.setPhph(7.8);
		reading.setAlkmgl(65.0);
		reading.setConduscm(500.0);
		reading.setBodmgl(3.0);
		reading.setNo2nmgl(0.009);
		reading.setCusol1mgl(0.0012);
		reading.setCusol2ugl(1.49);
		reading.setFesol1ugl(458.88);
		reading.setZnsolugl(5.0);
		reading.setTimestamp(LocalDateTime.now());

		 when(waterReadingService.getLatestRecord()).thenReturn(reading);

		// Act: Send GET request
		mockMvc.perform(get("/watermonitoring/records/latest"))

		// Assert: Verify if the response has the correct data and the status.
		.andExpect(status().isOk())  // checks if HTTP 200 or OK
		.andExpect(jsonPath("$.objectId").value(1556))
		.andExpect(jsonPath("$.phph").value(7.8))
		.andExpect(jsonPath("$.alkmgl").value(65.0))
		.andExpect(jsonPath("$.conduscm").value(500.0))
		.andExpect(jsonPath("$.bodmgl").value(3.0))
		.andExpect(jsonPath("$.no2nmgl").value(0.009))
		.andExpect(jsonPath("$.cusol1mgl").value(0.0012))
		.andExpect(jsonPath("$.cusol2ugl").value(1.49))
		.andExpect(jsonPath("$.fesol1ugl").value(458.88))
		.andExpect(jsonPath("$.znsolugl").value(5.0)); 
	}
	
	@Test
	void testGetLatestNoData() throws Exception {

		// Arrange: Mock the repository to return null value.
		when(waterReadingService.getLatestRecord()).thenReturn(null);

		// Act: Send GET request
		mockMvc.perform(get("/watermonitoring/records/latest"))

		//Assert: Check if the response status is 204 No Content
		.andExpect(status().isNoContent());  
	}
	
	@Test
	void testGetLatestCheck() throws Exception {

		// Arrange: Test two records with different timestamps
		WaterReading olderReading = new WaterReading(); 
		olderReading.setObjectId(1115);
		olderReading.setTimestamp(LocalDateTime.now().minusHours(1)); // Old timestamp

		WaterReading latestReading = new WaterReading();
		latestReading.setObjectId(1147);
		latestReading.setTimestamp(LocalDateTime.now()); // Latest timestamp

		when(waterReadingService.getLatestRecord()).thenReturn(latestReading);

		// Act: Send GET request to get the latest reading
		mockMvc.perform(get("/watermonitoring/records/latest"))

		// Assert: Verify that the response contains the latest records.
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.objectId").value(1147))
		.andExpect(jsonPath("$.timestamp").exists());
	}
	
	@Test
	void testGetLatestDatabaseError() throws Exception {
		when(waterReadingService.getLatestRecord())
		.thenThrow(new DataAccessResourceFailureException("Database connection failed")); // mock for database errors

		// Act: Send GET request and check the response.
		mockMvc.perform(get("/watermonitoring/records/latest"))

		//Assert: Check if the response 500 internal server with error message.
		.andExpect(status().isInternalServerError())
		.andExpect(jsonPath("$.message").value("Database error:Database connection failed"));
	}
	

	@Test
	void testGetLatestInternalServerError() throws Exception {
		when(waterReadingService.getLatestRecord())

		// Mock the runtime error 
		.thenThrow(new RuntimeException("Internal server error"));

		// Act: Send GET request and check the response.
		mockMvc.perform(get("/watermonitoring/records/latest"))

		//Assert: Check if the response status is 500 internal server error.
		.andExpect(status().isInternalServerError())
		.andExpect(jsonPath("$.message").value("Internal server error:Internal server error"));
	}
	
	
	@Test
	public void testAllRecordsSuccess() throws Exception {

		// Arrange
		WaterReading record1 = new WaterReading();
		record1.setObjectId(1115);
		record1.setPhph(7.7);
		record1.setAlkmgl(65.0);
		record1.setConduscm(500.0);
		record1.setBodmgl(3.0);
		record1.setNo2nmgl(0.009);
		record1.setCusol1mgl(0.012);
		record1.setCusol2ugl(1.49);
		record1.setFesol1ugl(458.88);
		record1.setZnsolugl(5.0);
		record1.setTimestamp(LocalDateTime.now());

		WaterReading record2 = new WaterReading();
		record2.setObjectId(1147);
		record2.setPhph(6.8);
		record2.setAlkmgl(68.0);
		record2.setConduscm(225.0);
		record2.setBodmgl(4.0);
		record2.setNo2nmgl(0.005);
		record2.setCusol1mgl(0.022);
		record2.setCusol2ugl(2.32);
		record2.setFesol1ugl(720.88);
		record2.setZnsolugl(5.0);
		record2.setTimestamp(LocalDateTime.now());

		List<WaterReading> mockRecords = Arrays.asList(record1, record2);
		when(waterReadingService.getAllRecords()).thenReturn(mockRecords);

		// Act
		mockMvc.perform(get("/watermonitoring/records")) // Send GET request

		// Assert
		.andExpect(status().isOk())  // Expect 200 OK
		.andExpect(jsonPath("$.length()").value(2)) // Check array length

		// Validate first record
		.andExpect(jsonPath("$[0].objectId").value(1115))
		.andExpect(jsonPath("$[0].phph").value(7.7))
		.andExpect(jsonPath("$[0].alkmgl").value(65.0))
		.andExpect(jsonPath("$[0].conduscm").value(500.0))
		.andExpect(jsonPath("$[0].bodmgl").value(3.0))
		.andExpect(jsonPath("$[0].no2nmgl").value(0.009))
		.andExpect(jsonPath("$[0].cusol1mgl").value(0.012))
		.andExpect(jsonPath("$[0].cusol2ugl").value(1.49))
		.andExpect(jsonPath("$[0].fesol1ugl").value(458.88))
		.andExpect(jsonPath("$[0].znsolugl").value(5.0))

		// Validate second record
		.andExpect(jsonPath("$[1].objectId").value(1147))
		.andExpect(jsonPath("$[1].phph").value(6.8))
		.andExpect(jsonPath("$[1].alkmgl").value(68.0))
		.andExpect(jsonPath("$[1].conduscm").value(225.0))
		.andExpect(jsonPath("$[1].bodmgl").value(4.0))
		.andExpect(jsonPath("$[1].no2nmgl").value(0.005))
		.andExpect(jsonPath("$[1].cusol1mgl").value(0.022))
		.andExpect(jsonPath("$[1].cusol2ugl").value(2.32))
		.andExpect(jsonPath("$[1].fesol1ugl").value(720.88))
		.andExpect(jsonPath("$[1].znsolugl").value(5.0));

	}
	
	@Test 
	void testGetAllRecordsNoData() throws Exception {
		// Arrange 
		when(waterReadingService.getAllRecords()).thenReturn(Collections.emptyList());

		// Act
		mockMvc.perform(get("/watermonitoring/records"))

		// Assert: Expect 204 No Content
		.andExpect(status().isNoContent()); 
	}
	
	/**
	 * Test for handling internal server error when fetching records.
	 */
	@Test 
	void testGetAllRecordsInternalServerError() throws Exception {
		// Arrange
		when(waterReadingService.getAllRecords())
		.thenThrow(new RuntimeException("Internal server error"));

		// Act
		mockMvc.perform(get("/watermonitoring/records"))
		
		// Assert
		.andExpect(status().isInternalServerError())
		.andExpect(jsonPath("$.message").value("Internal server error:Internal server error"));
	}
	
}
