package com.water.quality.check.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualityCheckResponse {
    private String safetyFlag;
    private Double calculatedTDS; // ✅ Changed from `double` to `Double`

    public String getSafetyFlag() {
        return safetyFlag;
    }

    public Double getCalculatedTDS() {
        return calculatedTDS;
    }
}
