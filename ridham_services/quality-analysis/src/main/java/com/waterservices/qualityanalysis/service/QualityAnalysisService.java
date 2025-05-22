package com.waterservices.qualityanalysis.service;

import com.waterservices.qualityanalysis.model.WaterQualityRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for analyzing water quality data.
 */
@Service
public class QualityAnalysisService {

    // TODO: Improve Logging with red flag,
    //  maybe some sort of global declaration in application properties
    private static final Logger logger = LoggerFactory.getLogger(QualityAnalysisService.class);

    private final RestTemplate restTemplate;

    public QualityAnalysisService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    private AnalysisResult latestAnalysisResult;

    @Value("${monitoring.service.url}")
    private String monitoringServiceUrl;
    @Value("${monitoring.service.endpoint.fetch.latest-record}")
    private String monitoringFetchLatestEndpoint;

    private String authHeader;

    // Thresholds - loaded from application.properties

    @Value("${water.quality.thresholds.ph.max}")
    private Double pHMaxThreshold;
    @Value("${water.quality.thresholds.ph.min}")
    private Double pHMinThreshold;
    @Value("${water.quality.thresholds.alkalinity.max}")
    private Double alkalinityMaxThreshold;
    @Value("${water.quality.thresholds.conductivity.max}")
    private Double conductivityMaxThreshold;
    @Value("${water.quality.thresholds.nitrite-n.max}")
    private Double nitriteNMaxThreshold;
    @Value("${water.quality.thresholds.tds.max}")
    private Double tdsMaxThreshold;
    @Value("${water.quality.thresholds.turbidity.max}")
    private Double turbidityMaxThreshold; // Todo: deal with this, we are not using it for now



    public AnalysisResult getLatestAnalysisResult() {
        analyzeWaterQuality();
        return latestAnalysisResult;
    }

    public Double getPHMaxThreshold() {
        return pHMaxThreshold;
    }

    public Double getPHMinThreshold() {
        return pHMinThreshold;
    }

    public Double getAlkalinityMaxThreshold() {
        return alkalinityMaxThreshold;
    }

    public Double getConductivityMaxThreshold() {
        return conductivityMaxThreshold;
    }

    public Double getNitriteNMaxThreshold() {
        return nitriteNMaxThreshold;
    }

    public Double getTdsMaxThreshold() {
        return tdsMaxThreshold;
    }

    public Double getTurbidityMaxThreshold() {
        return turbidityMaxThreshold;
    }

    public String getAuthHeader() {
        return authHeader;
    }
    public void setAuthHeader(String authToken) {
        this.authHeader = authToken;
    }
    /**
     * Analyzes water quality periodically.
     */
    //@Scheduled(fixedRate = 60000, initialDelay = 6000) // Run every 60 seconds
    public void analyzeWaterQualityPeriodically() {
       analyzeWaterQuality();
    }

    /**
     * Analyzes water quality.
     */
    public void analyzeWaterQuality() {
        logger.info("Starting periodic water quality analysis...");
        WaterQualityRecord latestRecord = fetchLatestWaterQualityRecord();
        if (latestRecord != null) {
            AnalysisResult analysisResult = analyzeRecordAndAlert(latestRecord);
            latestAnalysisResult = analysisResult;
            logWaterSafetyFlag(analysisResult);
        } else {
            logger.warn("No water quality record fetched from monitoring service.");
        }
    }

    /**
     * Fetches the latest water quality record from the monitoring service.
     * @return The latest water quality record.
     */
    public WaterQualityRecord fetchLatestWaterQualityRecord() {
        String apiUrl = monitoringServiceUrl + monitoringFetchLatestEndpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<WaterQualityRecord> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    requestEntity,
                    WaterQualityRecord.class
            );

            WaterQualityRecord record = response.getBody();
            if (record != null) {
                logger.info("Successfully fetched latest record: Object ID = {}", record.getObjectId());
            }
            return record;
        } catch (RestClientException e) {
            logger.error("Error fetching latest water quality record from Monitoring Service: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Analyzes a water quality record and generates alerts.
     * @param record The water quality record to analyze.
     * @return The analysis result.
     */
    public AnalysisResult analyzeRecordAndAlert(WaterQualityRecord record) {
        Map<String, String> alerts = checkThresholds(record);
        AnalysisResult analysisResult = new AnalysisResult();

        // Always calculate TDS
        Double tds = calculateTDS(record);
        if (tds != null) {
            analysisResult.setCalculatedTDS(tds);
        }

        if (!alerts.isEmpty()) {
            analysisResult.setSafeWater(false);
            StringBuilder alertMessage = new StringBuilder("Water Quality Alert!\n");
            alertMessage.append("Record ObjectId: ").append(record.getObjectId()).append("\n");
            alertMessage.append("Timestamp: ").append(record.getTimestamp()).append("\n");
            alerts.forEach((parameter, message) -> alertMessage.append("- ").append(parameter).append(": ").append(message).append("\n"));
            logger.warn(alertMessage.toString());
            analysisResult.setAlertMessages(alerts);
        } else {
            analysisResult.setSafeWater(true);
            logger.info("Water quality is within acceptable limits for record ObjectId: {}", record.getObjectId());
        }

        return analysisResult;
    }

    /**
     * Logs the water safety flag.
     * @param analysisResult The analysis result.
     */
    private void logWaterSafetyFlag(AnalysisResult analysisResult) {
        String flagColor = analysisResult.isSafeWater() ? "GREEN" : "RED";
        logger.info("Water Safety Flag: {}", flagColor);
    }

    /**
     * Checks if the water quality record exceeds any thresholds.
     * @param record The water quality record to check.
     * @return A map of alerts.
     */
    private Map<String, String> checkThresholds(WaterQualityRecord record) {
        Map<String, String> alerts = new HashMap<>();

        // Calculate TDS here, based on instructions: TDS = CUSOL1(mg/l) + CUSOL2(ug/l) + FESOL1(ug/l) + ZN SOL(ug/l)
        Double calculatedTDS = calculateTDS(record);

        if (record.getpH() != null) {
            if (record.getpH() > pHMaxThreshold) {
                alerts.put("pH", "pH level is too high: " + record.getpH() + ", threshold: " + pHMaxThreshold);
            } else if (record.getpH() < pHMinThreshold) {
                alerts.put("pH", "pH level is too low: " + record.getpH() + ", threshold: " + pHMinThreshold);
            }
        }
        if (record.getAlkalinity() != null && record.getAlkalinity() > alkalinityMaxThreshold) {
            alerts.put("Alkalinity", "Alkalinity is too high: " + record.getAlkalinity() + ", threshold: " + alkalinityMaxThreshold);
        }
        if (record.getConductivity() != null && record.getConductivity() > conductivityMaxThreshold) {
            alerts.put("Conductivity", "Conductivity is too high: " + record.getConductivity() + ", threshold: " + conductivityMaxThreshold);
        }
        if (record.getNitriteN() != null && record.getNitriteN() > nitriteNMaxThreshold) {
            alerts.put("Nitrite-N", "Nitrite-N is too high: " + record.getNitriteN() + ", threshold: " + nitriteNMaxThreshold);
        }
        if (calculatedTDS != null && calculatedTDS > tdsMaxThreshold) {
            alerts.put("Total Dissolved Solids (TDS)", "TDS is too high: " + calculatedTDS + " mg/L, threshold: " + tdsMaxThreshold + " mg/L");
        }

        return alerts;
    }

    /**
     * Calculates the Total Dissolved Solids (TDS) based on the water quality record.
     * @param record The water quality record.
     * @return The calculated TDS value, or null if any required field is missing.
     */
    public Double calculateTDS(WaterQualityRecord record) {
        if (record.getCopperMgL() == null || record.getCopperUgL() == null || record.getIronUgL() == null || record.getZincUgL() == null) {
            return null;
        }
        // TDS = CUSOL1(mg/l) + CUSOL2(ug/l) + FESOL1(ug/l) + ZN SOL(ug/l)
        // Converting ug/L to mg/L by dividing by 1000
        return record.getCopperMgL() + (record.getCopperUgL() / 1000.0) + (record.getIronUgL() / 1000.0) + (record.getZincUgL() / 1000.0);
    }
}