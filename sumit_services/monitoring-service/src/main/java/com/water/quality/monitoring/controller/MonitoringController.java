package com.water.quality.monitoring.controller;

import com.water.quality.monitoring.model.WaterQualityRecord;
import com.water.quality.monitoring.security.TokenValidationResponse;
import com.water.quality.monitoring.security.TokenValidator;
import com.water.quality.monitoring.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/water-quality")
@RequiredArgsConstructor
public class MonitoringController {

    private final MonitoringService monitoringService;

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


    @GetMapping("/records")
    public ResponseEntity<?> getAllRecords(@RequestHeader("Authorization") String authHeader) {
        ResponseEntity<?> authCheck = authenticateRequest(authHeader);
        if (authCheck != null) return authCheck;

        List<WaterQualityRecord> records = monitoringService.getAllRecords();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestRecord(@RequestHeader("Authorization") String authHeader) {
        ResponseEntity<?> authCheck = authenticateRequest(authHeader);
        if (authCheck != null) return authCheck;

        WaterQualityRecord latest = monitoringService.getLatestRecord();
        return ResponseEntity.ok(latest);
    }
}
