package com.water.quality.monitoring.repository;

import com.water.quality.monitoring.model.WaterQualityRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class WaterQualityRepositoryTest {

    @Autowired
    private WaterQualityRepository repository;

    @Test
    void testSaveAndRetrieveRecord() {
        WaterQualityRecord record = new WaterQualityRecord(2001, 7.0, 95.0, 260.0, 3.8, 1.0, 0.1, 0.4, 0.02, 0.01, LocalDateTime.now());
        repository.save(record);

        Optional<WaterQualityRecord> fetchedRecord = repository.findById(record.getId());

        assertTrue(fetchedRecord.isPresent(), "Record should exist in database");
        assertEquals(7.0, fetchedRecord.get().getPH(), "pH value should match");
    }

    @Test
    void testRecordDoesNotExist() {
        boolean exists = repository.existsByObjectId(9999);
        assertFalse(exists, "Record should not exist in database");
    }

    @Test
    void testFindByObjectIdAndTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        WaterQualityRecord record = new WaterQualityRecord(3001, 7.2, 98.0, 275.0, 2.5, 0.8, 0.15, 0.35, 0.03, 0.02, timestamp);
        repository.save(record);

        Optional<WaterQualityRecord> fetchedRecord = repository.findByObjectIdAndTimestamp(record.getObjectId(), timestamp);

        assertTrue(fetchedRecord.isPresent(), "Record should exist in database with matching timestamp");
    }
}
