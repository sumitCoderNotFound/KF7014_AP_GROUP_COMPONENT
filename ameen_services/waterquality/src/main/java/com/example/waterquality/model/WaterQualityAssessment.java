package com.example.waterquality.model;

import com.example.waterquality.constants.WhoLimits;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a water quality assessment result.
 * This class contains both the measured water quality parameters and their assessment results
 * based on WHO drinking water quality guidelines. It tracks various parameters like pH,
 * alkalinity, conductivity, and maintains a list of any quality issues found.
 */
public class WaterQualityAssessment {
    /** Unique identifier of the water quality measurement record being assessed */
    private String recordId;
    
    /** Identifier of the water body or measurement location */
    private String objectId;
    
    /** Timestamp when the water quality measurement was taken */
    private LocalDateTime timestamp;
    
    /** Overall status of the water quality assessment (GREEN for pass, RED for issues found) */
    private String status; // GREEN or RED

    // Current Parameters
    /** Current pH value of the water sample */
    private double ph;
    
    /** Current alkalinity in mg/L */
    private double alkalinity;
    
    /** Current conductivity in µS/cm */
    private double conductivity;
    
    /** Current nitrite concentration in mg/L */
    private double nitrite;
    
    /** Calculated total dissolved solids in mg/L */
    private double totalDissolvedSolids;

    // WHO Limits
    /** Minimum acceptable pH value as per WHO guidelines */
    private final double minPh = WhoLimits.MIN_PH;
    
    /** Maximum acceptable pH value as per WHO guidelines */
    private final double maxPh = WhoLimits.MAX_PH;
    
    /** Maximum acceptable alkalinity in mg/L as per WHO guidelines */
    private final double maxAlkalinity = WhoLimits.MAX_ALKALINITY_MG_L;
    
    /** Maximum acceptable conductivity in µS/cm as per WHO guidelines */
    private final double maxConductivity = WhoLimits.MAX_CONDUCTIVITY_US_CM;
    
    /** Maximum acceptable nitrite concentration in mg/L as per WHO guidelines */
    private final double maxNitrite = WhoLimits.MAX_NITRITE_MG_L;
    
    /** Maximum acceptable total dissolved solids in mg/L as per WHO guidelines */
    private final double maxTds = WhoLimits.MAX_TDS_MG_L;

    /** List of water quality issues found during assessment */
    private List<String> issues;

    /**
     * Default constructor.
     * Initializes an empty list for storing water quality issues.
     */
    public WaterQualityAssessment() {
        this.issues = new ArrayList<>();
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getIssues() {
        return issues;
    }

    public void addIssue(String issue) {
        this.issues.add(issue);
    }

    public double getTotalDissolvedSolids() {
        return totalDissolvedSolids;
    }

    public void setTotalDissolvedSolids(double totalDissolvedSolids) {
        this.totalDissolvedSolids = totalDissolvedSolids;
    }

    public double getPh() {
        return ph;
    }

    public void setPh(double ph) {
        this.ph = ph;
    }

    public double getAlkalinity() {
        return alkalinity;
    }

    public void setAlkalinity(double alkalinity) {
        this.alkalinity = alkalinity;
    }

    public double getConductivity() {
        return conductivity;
    }

    public void setConductivity(double conductivity) {
        this.conductivity = conductivity;
    }

    public double getNitrite() {
        return nitrite;
    }

    public void setNitrite(double nitrite) {
        this.nitrite = nitrite;
    }

    public double getMinPh() {
        return minPh;
    }

    public double getMaxPh() {
        return maxPh;
    }

    public double getMaxAlkalinity() {
        return maxAlkalinity;
    }

    public double getMaxConductivity() {
        return maxConductivity;
    }

    public double getMaxNitrite() {
        return maxNitrite;
    }

    public double getMaxTds() {
        return maxTds;
    }
} 