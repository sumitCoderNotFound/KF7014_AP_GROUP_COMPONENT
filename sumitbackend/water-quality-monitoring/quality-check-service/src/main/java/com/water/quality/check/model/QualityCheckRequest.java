package com.water.quality.check.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityCheckRequest {
    private Double pH;
    private Double alkalinity;
    private Double conductivity;
    private Double bod;
    private Double nitrite;
    private Double copperDissolved1;
    private Double copperDissolved2;
    private Double ironDissolved;
    private Double zincDissolved;
}
