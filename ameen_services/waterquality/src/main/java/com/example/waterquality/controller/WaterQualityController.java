package com.example.waterquality.controller;


import com.example.waterquality.model.WaterQualityAssessment;
import com.example.waterquality.service.WaterQualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for water quality assessment endpoints.
 * Provides APIs for retrieving water quality assessments, current status, and parameter measurements.
 * All assessments are based on WHO drinking water quality guidelines.
 * 
 * The controller offers three main endpoints:
 * - /assessment: Detailed quality assessment with all parameters and issues
 * - /status: Simple GREEN/RED status indicator
 * - /parameters: Current water quality parameters with their WHO limits
 */
@RestController
@RequestMapping("/api/quality") //"Water Quality Assessment API"
public class WaterQualityController {
    /** Service responsible for water quality assessment logic */
    @Autowired
    private WaterQualityService waterQualityService;


    /**
     * Get the latest water quality assessment.
     *
     * @return Get latest water quality assessment, otherwise a 404 response.
     */
    @GetMapping("/assessment")
    public ResponseEntity<WaterQualityAssessment> getWaterQualityAssessment() {
        WaterQualityAssessment assessment = waterQualityService.checkWaterQuality();
        if (assessment != null) {
            return ResponseEntity.ok(assessment);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get the current water safety status.
     *
     * The status is a simple GREEN/RED indicator based on WHO drinking water quality guidelines.
     *
     * @return GREEN if all parameters are within WHO limits, RED if any parameter exceeds those limits.
     */
    @GetMapping("/status")
    public ResponseEntity<String> getWaterQualityStatus() {
        WaterQualityAssessment assessment = waterQualityService.checkWaterQuality();
        if (assessment != null) {
            return ResponseEntity.ok(assessment.getStatus());
        }
        return ResponseEntity.notFound().build();
    }


    /**
     * Get the current water parameters with their WHO limits.
     *
     * @return All measured parameters with their WHO limits if available, otherwise a 404 response.
     */
    @GetMapping("/parameters")
    public ResponseEntity<WaterQualityAssessment> getWaterParameters() {
        WaterQualityAssessment assessment = waterQualityService.checkWaterQuality();
        if (assessment != null) {
            return ResponseEntity.ok(assessment);
        }
        return ResponseEntity.notFound().build();
    }
}
