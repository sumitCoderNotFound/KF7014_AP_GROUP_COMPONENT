package com.example.waterquality.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for water quality measurements.
 * This class represents a single water quality measurement record with various parameters
 * including pH, alkalinity, conductivity, and dissolved metal concentrations.
 * Field names in comments correspond to their database column names.
 */
public class WaterQualityDTO {
    /** Unique identifier for the measurement record */
    private String id;
    
    /** Timestamp when the measurement was taken */
    private LocalDateTime timestamp;
    
    /** Identifier of the water body or measurement location */
    private String objectId;
    
    /** pH value of the water sample (PHpH) */
    private double ph;
    
    /** Alkalinity in milligrams per liter (ALK_MGL) */
    private double alkalinity;
    
    /** Electrical conductivity in microsiemens per centimeter (COND_USCM) */
    private double conductivity;
    
    /** Biochemical Oxygen Demand in milligrams per liter (BOD_MGL) */
    private double bod;
    
    /** Nitrite concentration in milligrams per liter (NO2_N_MGL) */
    private double nitrite;
    
    /** Dissolved copper concentration in milligrams per liter (CUSOL1_MGL) */
    private double cusol1;
    
    /** Dissolved copper concentration in micrograms per liter (CUSOL2_UGL) */
    private double cusol2;
    
    /** Dissolved iron concentration in micrograms per liter (FESOL1_UGL) */
    private double fesol1;
    
    /** Dissolved zinc concentration in micrograms per liter (ZN_SOL_UGL) */
    private double znsol;

    /** Default constructor */
    public WaterQualityDTO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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

    public double getBod() {
        return bod;
    }

    public void setBod(double bod) {
        this.bod = bod;
    }

    public double getNitrite() {
        return nitrite;
    }

    public void setNitrite(double nitrite) {
        this.nitrite = nitrite;
    }

    public double getCusol1() {
        return cusol1;
    }

    public void setCusol1(double cusol1) {
        this.cusol1 = cusol1;
    }

    public double getCusol2() {
        return cusol2;
    }

    public void setCusol2(double cusol2) {
        this.cusol2 = cusol2;
    }

    public double getFesol1() {
        return fesol1;
    }

    public void setFesol1(double fesol1) {
        this.fesol1 = fesol1;
    }

    public double getZnsol() {
        return znsol;
    }

    public void setZnsol(double znsol) {
        this.znsol = znsol;
    }
}
