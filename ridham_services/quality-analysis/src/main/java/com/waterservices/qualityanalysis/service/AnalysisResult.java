package com.waterservices.qualityanalysis.service;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the outcome of analyzing a water quality record.
 */
@Schema(description = "Represents the result of water quality analysis.")
public class AnalysisResult {

    @Schema(description = "Indicates whether the water is safe.", example = "true")
    private boolean safeWater;

    @Schema(description = "Map of alert messages.", example = "{\"pH\": \"pH level is too high.\"}")
    private Map<String, String> alertMessages;

    public AnalysisResult() {
        this.safeWater = true;
        this.alertMessages = new HashMap<>();
    }

    public boolean isSafeWater() {
        return safeWater;
    }

    public void setSafeWater(boolean safeWater) {
        this.safeWater = safeWater;
    }

    public Map<String, String> getAlertMessages() {
        return alertMessages;
    }

    public void setAlertMessages(Map<String, String> alertMessages) {
        this.alertMessages = alertMessages;
    }
}