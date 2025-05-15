package com.waterservices.monitoring.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Represents water quality data stored in the 'water_quality' table.
 */
@Entity
@Table(name = "water_quality")
@Schema(description = "Represents water quality data.")
public class WaterQuality {

    public WaterQuality() {
        this.version = 0; // Initialize version to 0 for new entities
    }

    public WaterQuality(LocalDateTime timestamp,
                        Long objectId,
                        Double pH,
                        Double alkalinity,
                        Double conductivity,
                        Double bod,
                        Double nitriteN,
                        Double copperMgL,
                        Double copperUgL,
                        Double ironUgL,
                        Double zincUgL) {
        this.timestamp = timestamp;
        this.objectId = objectId;
        this.pH = pH;
        this.alkalinity = alkalinity;
        this.conductivity = conductivity;
        this.bod = bod;
        this.nitriteN = nitriteN;
        this.copperMgL = copperMgL;
        this.copperUgL = copperUgL;
        this.ironUgL = ironUgL;
        this.zincUgL = zincUgL;
        this.version = 0;
    }

    @Id
    @GeneratedValue
    @Schema(description = "Auto Generated, Unique identifier.", example = "1")
    private Long id;

    @Version
    @Schema(description = "Entity version for optimistic locking.", example = "0")
    private Integer version;

    @Column(name = "object_id")
    @Schema(description = "Object identifier.", example = "12345")
    private Long objectId;

    @Column(name = "timestamp")
    @Schema(description = "Timestamp of the measurement.", example = "2023-10-27T10:00:00")
    private LocalDateTime timestamp;

    @Column(name = "ph_level")
    @Schema(description = "pH level.", example = "7.2")
    private Double pH;

    @Column(name = "alkalinity_mg_l")
    @Schema(description = "Alkalinity in mg/L.", example = "150.0")
    private Double alkalinity;

    @Column(name = "conductivity_us_cm")
    @Schema(description = "Conductivity in µS/cm.", example = "300.0")
    private Double conductivity;

    @Column(name = "bod_mg_l")
    @Schema(description = "Biochemical oxygen demand in mg/L.", example = "2.0")
    private Double bod;

    @Column(name = "nitrite_n_mg_l")
    @Schema(description = "Nitrite-N in mg/L.", example = "0.1")
    private Double nitriteN;

    @Column(name = "copper_mg_l")
    @Schema(description = "Copper in mg/L.", example = "0.5")
    private Double copperMgL;

    @Column(name = "copper_ug_l")
    @Schema(description = "Copper in µg/L.", example = "500.0")
    private Double copperUgL;

    @Column(name = "iron_ug_l")
    @Schema(description = "Iron in µg/L.", example = "200.0")
    private Double ironUgL;

    @Column(name = "zinc_ug_l")
    @Schema(description = "Zinc in µg/L.", example = "100.0")
    private Double zincUgL;

    // Getters and setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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



    @Override
    public String toString() {
        return "WaterQuality{" +
                "objectId=" + objectId +
                ", pH=" + pH +
                ", alkalinity=" + alkalinity +
                ", conductivity=" + conductivity +
                ", bod=" + bod +
                ", nitriteN=" + nitriteN +
                ", copperMgL=" + copperMgL +
                ", copperUgL=" + copperUgL +
                ", ironUgL=" + ironUgL +
                ", zincUgL=" + zincUgL +
                ", version=" + version +
                ", timestamp=" + timestamp +
                '}';
    }

    public String toJson() {
        return "{" +
                "\"objectId\":" + objectId + "," +
                "\"pH\":" + pH + "," +
                "\"alkalinity\":" + alkalinity + "," +
                "\"conductivity\":" + conductivity + "," +
                "\"bod\":" + bod + "," +
                "\"nitriteN\":" + nitriteN + "," +
                "\"copperMgL\":" + copperMgL + "," +
                "\"copperUgL\":" + copperUgL + "," +
                "\"ironUgL\":" + ironUgL + "," +
                "\"zincUgL\":" + zincUgL + "," +
                "\"version\":" + version + "," +
                "\"timestamp\":\"" + timestamp + "\"" +
                "}";
    }

}