package com.waterservices.monitoring.repository;

import com.waterservices.monitoring.model.WaterQuality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link WaterQuality} entities.
 */
@Repository
public interface MonitoringRepository  extends JpaRepository<WaterQuality, Long> {

    /**
     * Finds WaterQuality by objectId.
     * @param objectId The object ID.
     * @return Optional WaterQuality.
     */
    Optional<WaterQuality> findByObjectId(Long objectId);

    /**
     * Finds the latest WaterQuality entry.
     * @return WaterQuality, the latest entry.
     */
    WaterQuality findFirstByOrderByObjectIdDesc();
}