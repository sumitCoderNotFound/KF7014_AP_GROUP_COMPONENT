package com.water.quality.monitoring.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "water_quality")
public class WaterQuality {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "object_id", nullable = false, unique = true)
    private String objectId;

    @Column(nullable = false)
    private double ph;

    @Column(nullable = false)
    private double alkalinity;

    @Column(nullable = false)
    private double conductivity;

    @Column(nullable = false)
    private double bod;

    @Column(nullable = false)
    private double nitrite;

    @Column(name = "cusol1_mgl", nullable = false)
    private double cusol1;

    @Column(name = "cusol2_ugl", nullable = false)
    private double cusol2;

    @Column(name = "fesol1_ugl", nullable = false)
    private double fesol1;

    @Column(name = "zn_sol_ugl", nullable = false)
    private double znsol;

    // Optional: For optimistic locking (only if you plan updates from multiple threads)
    // @Version
    // private int version;

    public WaterQuality() {}

    // Getters and Setters (no setId method to avoid manual ID setting)
    public String getId() { return id; }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WaterQuality)) return false;
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
