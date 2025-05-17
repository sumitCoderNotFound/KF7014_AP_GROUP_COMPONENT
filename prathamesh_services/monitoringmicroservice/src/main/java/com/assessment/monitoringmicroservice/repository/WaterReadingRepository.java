package com.assessment.monitoringmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;	
import org.springframework.stereotype.Repository;

import com.assessment.monitoringmicroservice.model.WaterReading;


/**
 * Repository interface manages the water quality readings in the database.
 * 
 * <p>
 * This interface extends {@link JpaRepository} to create, read update and delete operations
 * for {@link WaterReading}. It also has a custom query method
 * to retrieve the latest water quality record based on the the latest timestamp.
 * </p>
 * 
 * <p>
 * The repository interacts with the database and helps the controller
 * to fetch and save water quality readings in the database table.
 * </p>
 * 
 * @author Prathamesh Belnekar
 * @version 1.0
 */
@Repository
public interface WaterReadingRepository extends JpaRepository<WaterReading, String> {

	// Custom method for finding the latest record based on timestamp.
	WaterReading findTopByOrderByTimestampDesc();


}
