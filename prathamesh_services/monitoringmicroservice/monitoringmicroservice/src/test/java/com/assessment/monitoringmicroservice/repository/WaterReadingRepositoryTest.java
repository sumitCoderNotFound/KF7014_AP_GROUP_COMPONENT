package com.assessment.monitoringmicroservice.repository;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.assessment.monitoringmicroservice.model.WaterReading;

import static org.assertj.core.api.Assertions.assertThat;



/**
 * This class contains unit tests for checking the functionality of the
 * {@link WaterReadingRepository} in saving and retrieving the water quality records.
 * It makes sure that the repository is able to store records and retrieve the most recent
 * record based on the timestamp.
 * 
 * @author Prathamesh Belnekar
 * @version 1.0
 */
@ExtendWith(SpringExtension.class) // Enables spring support in JUnit 5
@DataJpaTest              
@ActiveProfiles("test")  // Specifies the profile "test" should be used for configurations
public class WaterReadingRepositoryTest {

	@Autowired
	private WaterReadingRepository waterReadingRepository;

	/**
	 * Tests if a WaterReading record can be saved to the database successfully.
	 * It checks if the record is not null, and if the fields are correctly saved in the table.
	 */
	@Test
	public void testSaveWaterReading() { 

		// Arrange: Create a WaterReading object with sample data 
		WaterReading waterReading = new WaterReading(); 
		waterReading.setObjectId(1115);
		waterReading.setPhph(7.7);
		waterReading.setAlkmgl(95.0);
		waterReading.setConduscm(300.5);
		waterReading.setBodmgl(275.0);
		waterReading.setNo2nmgl(0.011);
		waterReading.setCusol1mgl(0.02);
		waterReading.setCusol2ugl(1.5);
		waterReading.setFesol1ugl(0.8);
		waterReading.setZnsolugl(2.2);
		waterReading.setTimestamp(LocalDateTime.now());

		// Act: Save the object in the database table
		WaterReading savedReading = waterReadingRepository.save(waterReading);

		// Assert: Check is record in saved successfully with the correct values and is not null
		assertThat(savedReading).isNotNull();
		assertThat(savedReading.getId()).isNotNull(); // checks if ID value is generated 
		assertThat(savedReading.getObjectId()).isEqualTo(1115);
		assertThat(savedReading.getPhph()).isEqualTo(7.7);
		assertThat(savedReading.getAlkmgl()).isEqualTo(95.0);
		assertThat(savedReading.getConduscm()).isEqualTo(300.5);
		assertThat(savedReading.getBodmgl()).isEqualTo(275.0);
		assertThat(savedReading.getNo2nmgl()).isEqualTo(0.011);
		assertThat(savedReading.getCusol1mgl()).isEqualTo(0.02);
		assertThat(savedReading.getCusol2ugl()).isEqualTo(1.5);
		assertThat(savedReading.getFesol1ugl()).isEqualTo(0.8);
		assertThat(savedReading.getZnsolugl()).isEqualTo(2.2);
	}


	/**
	 * Tests the repository has the ability to retrieve the latest water reading record
	 * based on the timestamp. 
	 * It inserts two records with different date and time
	 * and checks if the most recent record is fetched properly.
	 */
	@Test
	public void testFindTheLatestWaterReading() {

		// Arrange: Inserting two records with different timestamps.
		WaterReading waterReadingOld = new WaterReading(); // Old Record
		waterReadingOld.setObjectId(1115);
		waterReadingOld.setPhph(7.8);
		waterReadingOld.setAlkmgl(95.0);
		waterReadingOld.setConduscm(300.5);
		waterReadingOld.setBodmgl(275.0);
		waterReadingOld.setNo2nmgl(0.011);
		waterReadingOld.setCusol1mgl(0.02);
		waterReadingOld.setCusol2ugl(1.5);
		waterReadingOld.setFesol1ugl(0.8);
		waterReadingOld.setZnsolugl(2.2);
		waterReadingOld.setTimestamp(LocalDateTime.now().minusDays(1)); 
		waterReadingRepository.save(waterReadingOld);

		WaterReading waterReadingLatest = new WaterReading(); // Latest Record
		waterReadingLatest.setObjectId(1148);
		waterReadingLatest.setPhph(7.0);
		waterReadingLatest.setAlkmgl(88.0);
		waterReadingLatest.setConduscm(234.5);
		waterReadingLatest.setBodmgl(278.0);
		waterReadingLatest.setNo2nmgl(0.041);
		waterReadingLatest.setCusol1mgl(0.02);
		waterReadingLatest.setCusol2ugl(2.5);
		waterReadingLatest.setFesol1ugl(0.12);
		waterReadingLatest.setZnsolugl(4.4);
		waterReadingLatest.setTimestamp(LocalDateTime.now());
		waterReadingRepository.save(waterReadingLatest);

		// Act: Recent reading based on timestamp
		WaterReading latestRecord = waterReadingRepository.findTopByOrderByTimestampDesc();

		// Assert: checks if the fetched data is the latest one 
		assertThat(latestRecord).isNotNull();
		assertThat(latestRecord.getObjectId()).isEqualTo(1148); 
		assertThat(latestRecord.getPhph()).isEqualTo(7.0);
		assertThat(latestRecord.getAlkmgl()).isEqualTo(88.0);
		assertThat(latestRecord.getConduscm()).isEqualTo(234.5);
		assertThat(latestRecord.getBodmgl()).isEqualTo(278.0);
		assertThat(latestRecord.getNo2nmgl()).isEqualTo(0.041);
		assertThat(latestRecord.getCusol1mgl()).isEqualTo(0.02);
		assertThat(latestRecord.getCusol2ugl()).isEqualTo(2.5);
		assertThat(latestRecord.getFesol1ugl()).isEqualTo(0.12);
		assertThat(latestRecord.getZnsolugl()).isEqualTo(4.4);
	}


	/**
	 * Tests if there is no data available in the repository.
	 * It verifies that if there are no records in the database, then the returned result will be null.
	 */
	@Test
	public void testNoDataAvaliable() {

		// Act: Retrieve the latest record
		WaterReading latestRecord = waterReadingRepository.findTopByOrderByTimestampDesc();

		// Assert: latestRecord should be null if there is no data.
		assertThat(latestRecord).isNull(); 
	}

}
