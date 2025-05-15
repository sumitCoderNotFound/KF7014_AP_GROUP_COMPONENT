package com.water.quality.check.controller;

import com.water.quality.check.client.MonitoringServiceClient;
import com.water.quality.check.model.QualityCheckRequest;
import com.water.quality.check.model.QualityCheckResponse;
import com.water.quality.check.service.QualityCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quality-check")
public class QualityCheckController {

    @Autowired
    private QualityCheckService qualityCheckService;

    @Autowired
    private MonitoringServiceClient monitoringServiceClient;

    @GetMapping("/validate")
    public ResponseEntity<QualityCheckResponse> validateWaterQuality() {
        QualityCheckRequest request = monitoringServiceClient.fetchLatestWaterQualityData();
        QualityCheckResponse response = qualityCheckService.validateWaterQuality(request);
        return ResponseEntity.ok(response);
    }
}
