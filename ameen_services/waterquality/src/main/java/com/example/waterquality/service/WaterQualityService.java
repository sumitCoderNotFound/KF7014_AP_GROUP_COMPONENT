package com.example.waterquality.service;

import com.example.waterquality.constants.WhoLimits;
import com.example.waterquality.dto.WaterQualityDTO;
import com.example.waterquality.model.WaterQualityAssessment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service class for handling water monitoring business logic.
 * This service implements the core functionality for water quality monitoring,
 * data processing, and analysis.
 */
@Service
public class WaterQualityService {
    private static final Logger logger = LoggerFactory.getLogger(WaterQualityService.class);
    private final RestTemplate restTemplate;
    private final Map<String, LocalDateTime> processedRecords = new ConcurrentHashMap<>();
    private LocalDateTime lastProcessedTime = LocalDateTime.now().minusHours(1); // Start from 1 hour ago

    @Value("${monitoring.service.url}")
    private String monitoringServiceUrl;

    public WaterQualityService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 30000) // Check every 30 seconds
    public WaterQualityAssessment checkWaterQuality() {
        logger.info("Checking water quality from monitoring service");
        
        try {
            ResponseEntity<WaterQualityDTO[]> response =
                restTemplate.getForEntity(monitoringServiceUrl + "/records", WaterQualityDTO[].class);

            WaterQualityDTO[] records = response.getBody();
            if (Objects.nonNull(records) && records.length > 0) {
                // Find the newest unprocessed record
                Optional<WaterQualityDTO> latestRecord = Arrays.stream(records)
                    .filter(record -> record.getTimestamp().isAfter(lastProcessedTime) &&
                            !processedRecords.containsKey(record.getId()))
                    .max(Comparator.comparing(WaterQualityDTO::getTimestamp));

                if (latestRecord.isPresent()) {
                    WaterQualityDTO record = latestRecord.get();
                    // Update tracking
                    lastProcessedTime = record.getTimestamp();
                    processedRecords.put(record.getId(), record.getTimestamp());
                    
                    // Clean up old records (keep last 24 hours)
                    LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
                    processedRecords.entrySet().removeIf(entry -> entry.getValue().isBefore(cutoff));
                    
                    logger.info("Processing new record with ID: {} from timestamp: {}", 
                        record.getId(), record.getTimestamp());
                    return assessWaterQuality(record);
                } else {
                    logger.info("No new records to process since: {}", lastProcessedTime);
                    return null;
                }
            }
            
            logger.warn("No water quality records found");
            return null;
        } catch (Exception e) {
            logger.error("Error checking water quality", e);
            throw new RuntimeException("Failed to check water quality", e);
        }
    }

    private WaterQualityAssessment assessWaterQuality(WaterQualityDTO record) {
        WaterQualityAssessment assessment = new WaterQualityAssessment();
        assessment.setRecordId(record.getId());
        assessment.setTimestamp(record.getTimestamp());
        assessment.setObjectId(record.getObjectId());

        // Set current parameter values
        assessment.setPh(record.getPh());
        assessment.setAlkalinity(record.getAlkalinity());
        assessment.setConductivity(record.getConductivity());
        assessment.setNitrite(record.getNitrite());

        // Check pH (6.5 - 8.5)
        if (record.getPh() < WhoLimits.MIN_PH || record.getPh() > WhoLimits.MAX_PH) {
            assessment.addIssue(String.format("pH (%.2f) outside safe range %.2f-%.2f",
                record.getPh(), WhoLimits.MIN_PH, WhoLimits.MAX_PH));
        }

        // Check Alkalinity (max 500 mg/l)
        if (record.getAlkalinity() > WhoLimits.MAX_ALKALINITY_MG_L) {
            assessment.addIssue(String.format("Alkalinity (%.2f mg/L) exceeds limit of %.2f mg/L",
                record.getAlkalinity(), WhoLimits.MAX_ALKALINITY_MG_L));
        }

        // Check Conductivity (max 2000 µS/cm)
        if (record.getConductivity() > WhoLimits.MAX_CONDUCTIVITY_US_CM) {
            assessment.addIssue(String.format("Conductivity (%.2f µS/cm) exceeds limit of %.2f µS/cm",
                record.getConductivity(), WhoLimits.MAX_CONDUCTIVITY_US_CM));
        }

        // Check Nitrite (max 1 mg/l)
        if (record.getNitrite() > WhoLimits.MAX_NITRITE_MG_L) {
            assessment.addIssue(String.format("Nitrite (%.2f mg/L) exceeds limit of %.2f mg/L",
                record.getNitrite(), WhoLimits.MAX_NITRITE_MG_L));
        }

        // Calculate and check Total Dissolved Solids (max 1000 mg/l)
        double tds = calculateTotalDissolvedSolids(record);
        assessment.setTotalDissolvedSolids(tds);
        
        if (tds > WhoLimits.MAX_TDS_MG_L) {
            assessment.addIssue(String.format("Total Dissolved Solids (%.2f mg/L) exceeds limit of %.2f mg/L",
                tds, WhoLimits.MAX_TDS_MG_L));
        }

        // Set final status (GREEN if all parameters within limits, RED if any parameter exceeds limits)
        assessment.setStatus(assessment.getIssues().isEmpty() ? "GREEN" : "RED");
        
        logger.info("Water quality assessment completed for record {}. Status: {}, Issues count: {}", 
            record.getObjectId(), assessment.getStatus(), assessment.getIssues().size());
        
        if (!assessment.getIssues().isEmpty()) {
            logger.warn("Water quality issues found for record {}: {}", 
                record.getObjectId(), String.join("; ", assessment.getIssues()));
        }
        
        return assessment;
    }

    private double calculateTotalDissolvedSolids(WaterQualityDTO record) {
        // Convert all values to mg/L and sum
        double cusol1_mg = record.getCusol1();                    // Already in mg/L
        double cusol2_mg = record.getCusol2() * WhoLimits.UG_L_TO_MG_L;  // Convert µg/L to mg/L
        double fesol1_mg = record.getFesol1() * WhoLimits.UG_L_TO_MG_L;  // Convert µg/L to mg/L
        double znsol_mg = record.getZnsol() * WhoLimits.UG_L_TO_MG_L;    // Convert µg/L to mg/L
        
        double totalTds = cusol1_mg + cusol2_mg + fesol1_mg + znsol_mg;
        
        logger.debug("TDS Calculation - CUSOL1: {} mg/L, CUSOL2: {} mg/L, FESOL1: {} mg/L, ZNSOL: {} mg/L, Total: {} mg/L",
            cusol1_mg, cusol2_mg, fesol1_mg, znsol_mg, totalTds);
            
        return totalTds;
    }
}
