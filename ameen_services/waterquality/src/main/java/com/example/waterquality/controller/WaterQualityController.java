package com.example.waterquality.controller;


import com.example.waterquality.model.WaterQualityAssessment;
import com.example.waterquality.security.TokenValidationResponse;
import com.example.waterquality.security.TokenValidator;
import com.example.waterquality.service.WaterQualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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


    @Autowired
    private TokenValidator tokenValidator;


    private ResponseEntity<?> authenticateRequest(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        TokenValidationResponse validation = tokenValidator.validateToken(token);

        if (!validation.isValid()) {
            if ("expired".equals(validation.getReason())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access token expired");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token");
            }
        }

        // Token is valid
        return null;
    }

    /**
     * Get the latest water quality assessment.
     *
     * @return Get latest water quality assessment, otherwise a 404 response.
     */
    @GetMapping("/assessment")
    public ResponseEntity<?> getWaterQualityAssessment(@RequestHeader("Authorization") String authHeader) {
        ResponseEntity<?> authCheck = authenticateRequest(authHeader);
        if (authCheck != null) return authCheck;

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
    public ResponseEntity<?> getWaterQualityStatus(@RequestHeader("Authorization") String authHeader) {
        ResponseEntity<?> authCheck = authenticateRequest(authHeader);
        if (authCheck != null) return authCheck;

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
    public ResponseEntity<?> getWaterParameters(@RequestHeader("Authorization") String authHeader) {
        ResponseEntity<?> authCheck = authenticateRequest(authHeader);
        if (authCheck != null) return authCheck;

        WaterQualityAssessment assessment = waterQualityService.checkWaterQuality();
        if (assessment != null) {
            return ResponseEntity.ok(assessment);
        }
        return ResponseEntity.notFound().build();
    }
}
