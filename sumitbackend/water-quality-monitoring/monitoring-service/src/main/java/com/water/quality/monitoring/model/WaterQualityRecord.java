package com.water.quality.monitoring.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(collection = "water_quality_records")
public class WaterQualityRecord {

    @Id
    private String id;

    private int objectId;

    @JsonProperty("pH")
    private Double pH;

    private Double alkalinity;
    private Double conductivity;
    private Double bod;
    private Double nitrite;
    private Double copperDissolved1;
    private Double copperDissolved2;
    private Double ironDissolved;
    private Double zincDissolved;
    private LocalDateTime timestamp;

    public WaterQualityRecord(int objectId, Double pH, Double alkalinity, Double conductivity,
                              Double bod, Double nitrite, Double copperDissolved1, Double copperDissolved2,
                              Double ironDissolved, Double zincDissolved, LocalDateTime timestamp) {
        this.id = UUID.randomUUID().toString();
        this.objectId = objectId;
        this.pH = pH;
        this.alkalinity = alkalinity;
        this.conductivity = conductivity;
        this.bod = bod;
        this.nitrite = nitrite;
        this.copperDissolved1 = copperDissolved1;
        this.copperDissolved2 = copperDissolved2;
        this.ironDissolved = ironDissolved;
        this.zincDissolved = zincDissolved;
        this.timestamp = (timestamp != null) ? timestamp : LocalDateTime.now();
    }
}
