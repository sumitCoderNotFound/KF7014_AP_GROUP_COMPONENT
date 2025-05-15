package com.waterservices.qualityanalysis.controller;

import com.waterservices.qualityanalysis.service.AnalysisResult;
import com.waterservices.qualityanalysis.service.QualityAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for water quality analysis.
 */
@RestController
@RequestMapping("/api/quality-analysis")
public class QualityAnalysisController {

    private final QualityAnalysisService qualityAnalysisService;

    public QualityAnalysisController(QualityAnalysisService qualityAnalysisService) {
        this.qualityAnalysisService = qualityAnalysisService;
    }

    /**
     * Retrieves the latest analysis result.
     * @return ResponseEntity with AnalysisResult.
     */
    @GetMapping("/records/latest/analysis")
    @Operation(
            summary = "Retrieves the latest analysis result",
            description = "Returns the most recent analysis result.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Result found.", content = @Content(schema = @Schema(implementation = AnalysisResult.class))),
                    @ApiResponse(responseCode = "404", description = "Analysis result not found.")
            }
    )
    public ResponseEntity<AnalysisResult> getLatestRecordAnalysisResult() {
        if (qualityAnalysisService.getLatestAnalysisResult() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(qualityAnalysisService.getLatestAnalysisResult());
    }

    /**
     * Retrieves the thresholds used for water quality analysis.
     * @return ResponseEntity with a map of thresholds.
     */
    @GetMapping("/thresholds")
    @Operation(
            summary = "Retrieves water quality thresholds",
            description = "Returns a map of threshold values used for water quality analysis.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thresholds retrieved.", content = @Content(schema = @Schema(type = "object")))
            }
    )
    public ResponseEntity<Map<String, Double>> getThresholds() {
        Map<String, Double> thresholds = new HashMap<>();
        thresholds.put("pHMax", qualityAnalysisService.getPHMaxThreshold());
        thresholds.put("pHMin", qualityAnalysisService.getPHMinThreshold());
        thresholds.put("alkalinityMax", qualityAnalysisService.getAlkalinityMaxThreshold());
        thresholds.put("conductivityMax", qualityAnalysisService.getConductivityMaxThreshold());
        thresholds.put("nitriteNMax", qualityAnalysisService.getNitriteNMaxThreshold());
        thresholds.put("tdsMax", qualityAnalysisService.getTdsMaxThreshold());
        thresholds.put("turbidityMax", qualityAnalysisService.getTurbidityMaxThreshold());

        return ResponseEntity.ok(thresholds);
    }
}