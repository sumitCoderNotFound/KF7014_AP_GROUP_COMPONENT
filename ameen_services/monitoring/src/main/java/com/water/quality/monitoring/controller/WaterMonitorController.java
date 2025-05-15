package com.water.quality.monitoring.controller;

import com.water.quality.monitoring.entity.WaterQuality;
import com.water.quality.monitoring.repository.WaterMonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class WaterMonitorController {

    @Autowired
    private WaterMonitorRepository repository;

    /**
     * Endpoint to retrieve all water quality records.
     *
     * @return a list of all WaterQuality entities stored in the database.
     *
     */

    @GetMapping("/records")
    public List<WaterQuality> getAllRecords() {
        return repository.findAll();
    }
}