package com.water.quality.monitoring.controller;

import com.water.quality.monitoring.entity.WaterQuality;
import com.water.quality.monitoring.repository.WaterMonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/records")
public class WaterMonitorController {

    @Autowired
    private WaterMonitorRepository repository;

    @GetMapping
    public List<WaterQuality> getAllRecords() {
        try {
            return repository.findAll(); // ✅ CORRECT method name
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("DB fetch error: " + e.getMessage());
        }
    }
}
