package com.water.quality.monitoring.controller;

import com.water.quality.monitoring.entity.WaterQuality;
import com.water.quality.monitoring.repository.WaterMonitorRepository;
import com.water.quality.monitoring.security.TokenValidationResponse;
import com.water.quality.monitoring.security.TokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class WaterMonitorController {

    @Autowired
    private WaterMonitorRepository repository;

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
     * Endpoint to retrieve all water quality records.
     *
     * @return a list of all WaterQuality entities stored in the database.
     *
     */

    @GetMapping("/records")
    public ResponseEntity<?> getAllRecords(@RequestHeader("Authorization") String authHeader) {
        ResponseEntity<?> authCheck = authenticateRequest(authHeader);
        if (authCheck != null) return authCheck;

        return ResponseEntity.ok(repository.findAll());
    }
}