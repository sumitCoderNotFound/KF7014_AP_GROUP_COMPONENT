package com.water.quality.monitoring.repository;

import com.water.quality.monitoring.entity.WaterQuality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WaterMonitorRepository extends JpaRepository<WaterQuality, String> {
    Optional<WaterQuality> findByObjectId(String objectId);
}
