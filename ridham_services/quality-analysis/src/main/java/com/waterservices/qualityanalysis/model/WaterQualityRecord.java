package com.waterservices.qualityanalysis.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Represents a record of water quality measurements for analysis.
 */
@Schema(description = "Represents a water quality record for analysis.")
public class WaterQualityRecord {

    @Schema(description = "Object identifier.", example = "12345")
    private Long objectId;

    @Schema(description = "Timestamp of the measurement.", example = "2023-10-27T10:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "pH level.", example = "7.2")
    private Double pH;

    @Schema(description = "Alkalinity in mg/L.", example = "150.0")
    private Double alkalinity;

    @Schema(description = "Conductivity in µS/cm.", example = "300.0")
    private Double conductivity;

    @Schema(description = "Biochemical oxygen demand in mg/L.", example = "2.0")
    private Double bod;

    @Schema(description = "Nitrite-N in mg/L.", example = "0.1")
    private Double nitriteN;

    @Schema(description = "Copper in mg/L.", example = "0.5")
    private Double copperMgL;

    @Schema(description = "Copper in µg/L.", example = "500.0")
    private Double copperUgL;

    @Schema(description = "Iron in µg/L.", example = "200.0")
    private Double ironUgL;

    @Schema(description = "Zinc in µg/L.", example = "100.0")
    private Double zincUgL;

    public WaterQualityRecord() {
    }

    public WaterQualityRecord(Long objectId,
                              LocalDateTime timestamp,
                              Double pH,
                              Double alkalinity,
                              Double conductivity,
                              Double bod,
                              Double nitriteN,
                              Double copperMgL,
                              Double copperUgL,
                              Double ironUgL,
                              Double zincUgL) {
        this.objectId = objectId;
        this.timestamp = timestamp;
        this.pH = pH;
        this.alkalinity = alkalinity;
        this.conductivity = conductivity;
        this.bod = bod;
        this.nitriteN = nitriteN;
        this.copperMgL = copperMgL;
        this.copperUgL = copperUgL;
        this.ironUgL = ironUgL;
        this.zincUgL = zincUgL;
    }

    // Getters and setters...

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Double getpH() {
        return pH;
    }

    public void setpH(Double pH) {
        this.pH = pH;
    }

    public Double getAlkalinity() {
        return alkalinity;
    }

    public void setAlkalinity(Double alkalinity) {
        this.alkalinity = alkalinity;
    }

    public Double getConductivity() {
        return conductivity;
    }

    public void setConductivity(Double conductivity) {
        this.conductivity = conductivity;
    }

    public Double getBod() {
        return bod;
    }

    public void setBod(Double bod) {
        this.bod = bod;
    }

    public Double getNitriteN() {
        return nitriteN;
    }

    public void setNitriteN(Double nitriteN) {
        this.nitriteN = nitriteN;
    }

    public Double getCopperMgL() {
        return copperMgL;
    }

    public void setCopperMgL(Double copperMgL) {
        this.copperMgL = copperMgL;
    }

    public Double getCopperUgL() {
        return copperUgL;
    }

    public void setCopperUgL(Double copperUgL) {
        this.copperUgL = copperUgL;
    }

    public Double getIronUgL() {
        return ironUgL;
    }

    public void setIronUgL(Double ironUgL) {
        this.ironUgL = ironUgL;
    }

    public Double getZincUgL() {
        return zincUgL;
    }

    public void setZincUgL(Double zincUgL) {
        this.zincUgL = zincUgL;
    }
}