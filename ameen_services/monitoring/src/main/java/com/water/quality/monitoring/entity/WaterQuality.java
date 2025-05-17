package com.water.quality.monitoring.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity class representing water quality measurements.
 * This class is mapped to the "water_quality" table in the database.
 */
@Entity
@Table(name = "water_quality_data")  // Explicit table mapping
public class WaterQuality {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Automatically generate unique UUIDs
    private String id;

    @Column(nullable = false)
    private LocalDateTime timestamp;  // Date and time of the recorded measurement

    @Column(name = "object_id", nullable = false)
    private String objectId;  // Unique identifier from the original CSV data

    @Column(nullable = false)
    private double ph;  // pH level of the water sample

    @Column(nullable = false)
    private double alkalinity;  // Alkalinity concentration in mg/L

    @Column(nullable = false)
    private double conductivity;  // Electrical conductivity of water in µS/cm

    @Column(nullable = false)
    private double bod;  // Biochemical Oxygen Demand (BOD) in mg/L

    @Column(nullable = false)
    private double nitrite;  // Nitrite concentration (NO2-N) in mg/L

    @Column(name = "cusol1_mgl", nullable = false)
    private double cusol1;  // Dissolved Copper concentration in mg/L

    @Column(name = "cusol2_ugl", nullable = false)
    private double cusol2;  // Dissolved Copper concentration in µg/L

    @Column(name = "fesol1_ugl", nullable = false)
    private double fesol1;  // Dissolved Iron concentration in µg/L

    @Column(name = "zn_sol_ugl", nullable = false)
    private double znsol;  // Dissolved Zinc concentration in µg/L

    // Default constructor
    public WaterQuality() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getObjectId() { return objectId; }
    public void setObjectId(String objectId) { this.objectId = objectId; }

    public double getPh() { return ph; }
    public void setPh(double ph) { this.ph = ph; }

    public double getAlkalinity() { return alkalinity; }
    public void setAlkalinity(double alkalinity) { this.alkalinity = alkalinity; }

    public double getConductivity() { return conductivity; }
    public void setConductivity(double conductivity) { this.conductivity = conductivity; }

    public double getBod() { return bod; }
    public void setBod(double bod) { this.bod = bod; }

    public double getNitrite() { return nitrite; }
    public void setNitrite(double nitrite) { this.nitrite = nitrite; }

    public double getCusol1() { return cusol1; }
    public void setCusol1(double cusol1) { this.cusol1 = cusol1; }

    public double getCusol2() { return cusol2; }
    public void setCusol2(double cusol2) { this.cusol2 = cusol2; }

    public double getFesol1() { return fesol1; }
    public void setFesol1(double fesol1) { this.fesol1 = fesol1; }

    public double getZnsol() { return znsol; }
    public void setZnsol(double znsol) { this.znsol = znsol; }

    // Override equals and hashCode for object comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaterQuality that = (WaterQuality) o;
        return Double.compare(that.ph, ph) == 0 &&
                Double.compare(that.alkalinity, alkalinity) == 0 &&
                Double.compare(that.conductivity, conductivity) == 0 &&
                Double.compare(that.bod, bod) == 0 &&
                Double.compare(that.nitrite, nitrite) == 0 &&
                Double.compare(that.cusol1, cusol1) == 0 &&
                Double.compare(that.cusol2, cusol2) == 0 &&
                Double.compare(that.fesol1, fesol1) == 0 &&
                Double.compare(that.znsol, znsol) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(objectId, that.objectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, objectId, ph, alkalinity, conductivity, bod, nitrite, cusol1, cusol2, fesol1, znsol);
    }

    // Override toString for easy debugging and logging
    @Override
    public String toString() {
        return "WaterQuality{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", objectId='" + objectId + '\'' +
                ", ph=" + ph +
                ", alkalinity=" + alkalinity +
                ", conductivity=" + conductivity +
                ", bod=" + bod +
                ", nitrite=" + nitrite +
                ", cusol1=" + cusol1 +
                ", cusol2=" + cusol2 +
                ", fesol1=" + fesol1 +
                ", znsol=" + znsol +
                '}';
    }
}
