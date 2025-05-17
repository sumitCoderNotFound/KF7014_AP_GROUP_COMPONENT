package com.water.quality.monitoring.repository;

import com.water.quality.monitoring.model.WaterQualityRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WaterQualityRepository extends MongoRepository<WaterQualityRecord, String> {

    boolean existsByObjectId(int objectId);

    Optional<WaterQualityRecord> findTopByOrderByTimestampDesc();

    Optional<WaterQualityRecord> findByObjectIdAndTimestamp(int objectId, LocalDateTime timestamp);
}
