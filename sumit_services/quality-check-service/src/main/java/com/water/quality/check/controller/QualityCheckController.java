package com.water.quality.check.controller;

import com.water.quality.check.client.MonitoringServiceClient;
import com.water.quality.check.model.QualityCheckRequest;
import com.water.quality.check.model.QualityCheckResponse;
import com.water.quality.check.security.TokenValidationResponse;
import com.water.quality.check.security.TokenValidator;
import com.water.quality.check.service.QualityCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quality-check")
public class QualityCheckController {

    @Autowired
    private QualityCheckService qualityCheckService;

    @Autowired
    private MonitoringServiceClient monitoringServiceClient;

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


    @GetMapping("/validate")
    public ResponseEntity<?> validateWaterQuality(@RequestHeader("Authorization") String authHeader) {
        ResponseEntity<?> authCheck = authenticateRequest(authHeader);
        if (authCheck != null) return authCheck;

        QualityCheckRequest request = monitoringServiceClient.fetchLatestWaterQualityData(authHeader);
        QualityCheckResponse response = qualityCheckService.validateWaterQuality(request);
        return ResponseEntity.ok(response);
    }
}
