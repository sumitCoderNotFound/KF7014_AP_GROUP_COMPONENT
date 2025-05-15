package com.waterservices.monitoring.controller;

import com.waterservices.monitoring.model.WaterQuality;
import com.waterservices.monitoring.service.MonitoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Todo: Do we need more endpoints??!! POST PUT DELETE ?!
//  Won't make sense in context of the brief though

/**
 * Controller for water quality monitoring data.
 */
@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {

    private final MonitoringService monitoringService;

    public MonitoringController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    /**
     * Retrieves all water quality records.
     *
     * @return ResponseEntity with list of WaterQuality.
     */
    @GetMapping("/records")
    @Operation(
            summary = "Retrieves all water quality records",
            description = "Returns a list of all water quality records stored in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved all water quality records.", content = @Content(schema = @Schema(implementation = WaterQuality.class))),
                    @ApiResponse(responseCode = "204", description = "No water quality records found.")
            }
    )
    public ResponseEntity<List<WaterQuality>> getAllRecords() {
        List<WaterQuality> records = monitoringService.getAllWaterQualityRecords();
        if (!records.isEmpty()) {
            return ResponseEntity.ok(records);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Retrieves the latest water quality record.
     *
     * @return ResponseEntity with WaterQuality.
     */
    @GetMapping("/records/latest")
    @Operation(
            summary = "Retrieves the latest water quality record",
            description = "Returns the most recent water quality record.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the latest water quality record.", content = @Content(schema = @Schema(implementation = WaterQuality.class))),
                    @ApiResponse(responseCode = "204", description = "No water quality records found.")
            }
    )
    public ResponseEntity<WaterQuality> getLatestRecord() {
        WaterQuality record = monitoringService.getLatestWaterQualityRecord();
        if (record != null) {
            return ResponseEntity.ok(record);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}