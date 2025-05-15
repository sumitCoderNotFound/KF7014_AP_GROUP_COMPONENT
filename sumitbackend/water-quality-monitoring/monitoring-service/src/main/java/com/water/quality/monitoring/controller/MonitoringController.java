package com.water.quality.monitoring.controller;

import com.water.quality.monitoring.model.WaterQualityRecord;
import com.water.quality.monitoring.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/water-quality")
@RequiredArgsConstructor
public class MonitoringController {

    private final MonitoringService monitoringService;

    @GetMapping("/records")
    public ResponseEntity<List<WaterQualityRecord>> getAllRecords() {
        List<WaterQualityRecord> records = monitoringService.getAllRecords();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/latest")
    public ResponseEntity<WaterQualityRecord> getLatestRecord() {
        WaterQualityRecord latest = monitoringService.getLatestRecord();
        return ResponseEntity.ok(latest);
    }
}
