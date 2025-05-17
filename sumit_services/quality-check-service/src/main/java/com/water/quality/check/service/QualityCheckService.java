package com.water.quality.check.service;

import com.water.quality.check.model.QualityCheckRequest;
import com.water.quality.check.model.QualityCheckResponse;
import org.springframework.stereotype.Service;

@Service
public class QualityCheckService {

    public QualityCheckResponse validateWaterQuality(QualityCheckRequest request) {
        if (request == null) {
            return new QualityCheckResponse("RED", null); // Return RED if request is completely null
        }

        boolean isSafe = isWithinSafeLimits(request);
        Double tds = calculateTDS(request);

        return new QualityCheckResponse(isSafe ? "GREEN" : "RED", tds);
    }

    private boolean isWithinSafeLimits(QualityCheckRequest request) {
        if (request == null) return false;

        System.out.println("🔍 Debug: Checking Safety Limits...");

        Double pH = request.getPH();
        Double alkalinity = request.getAlkalinity();
        Double conductivity = request.getConductivity();
        Double bod = request.getBod();
        Double nitrite = request.getNitrite();
        Double copperDissolved1 = request.getCopperDissolved1();
        Double copperDissolved2 = request.getCopperDissolved2();
        Double ironDissolved = request.getIronDissolved();
        Double zincDissolved = request.getZincDissolved();

        // ✅ Print debug logs safely, replacing null values with "N/A"
        System.out.println("pH: " + (pH != null ? pH : "N/A") + " (should be between 6.5 and 8.5)");
        System.out.println("Alkalinity: " + (alkalinity != null ? alkalinity : "N/A") + " (should be <= 120)");
        System.out.println("Conductivity: " + (conductivity != null ? conductivity : "N/A") + " (should be <= 1000)");
        System.out.println("BOD: " + (bod != null ? bod : "N/A") + " (should be <= 3)");
        System.out.println("Nitrite: " + (nitrite != null ? nitrite : "N/A") + " (should be <= 1)");
        System.out.println("CopperDissolved1: " + (copperDissolved1 != null ? copperDissolved1 : "N/A") + " (should be <= 1.3)");
        System.out.println("CopperDissolved2: " + (copperDissolved2 != null ? copperDissolved2 : "N/A") + " (should be <= 1.3)");
        System.out.println("IronDissolved: " + (ironDissolved != null ? ironDissolved : "N/A") + " (should be <= 0.3)");
        System.out.println("ZincDissolved: " + (zincDissolved != null ? zincDissolved : "N/A") + " (should be <= 5)");

        // ✅ Allow null values to be considered safe
        boolean result = isWithinRange(pH, 6.5, 8.5) &&
                isWithinRange(alkalinity, 0.0, 120.0) &&
                isWithinRange(conductivity, 0.0, 1000.0) &&
                isWithinRange(bod, 0.0, 3.0) &&
                isWithinRange(nitrite, 0.0, 1.0) &&
                isWithinRange(copperDissolved1, 0.0, 1.3) &&
                isWithinRange(copperDissolved2, 0.0, 1.3) &&
                isWithinRange(ironDissolved, 0.0, 0.3) &&
                isWithinRange(zincDissolved, 0.0, 5.0);

        System.out.println("🔍 Safety Flag Result: " + (result ? "GREEN ✅" : "RED ❌"));
        return result;
    }

    private boolean isWithinRange(Double value, Double min, Double max) {
        if (value == null) return true; // ✅ Treat null as safe
        return (min == null || value >= min) && (max == null || value <= max);
    }

    private Double calculateTDS(QualityCheckRequest request) {
        if (request == null) return 0.0; // 🔥 Handle null request case by returning 0.0

        Double tds = sumIfNotNull(request.getAlkalinity(),
                request.getConductivity(),
                request.getBod(),
                request.getNitrite(),
                request.getCopperDissolved1(),
                request.getCopperDissolved2(),
                request.getIronDissolved(),
                request.getZincDissolved());

        System.out.println("🔍 Debug: Calculated TDS = " + (tds != null ? tds : "N/A"));
        return tds != null ? tds : 0.0; // ✅ Ensure TDS is always a valid number
    }

    // ✅ Helper method to handle multiple null values properly
    private Double sumIfNotNull(Double... values) {
        double sum = 0.0;
        boolean hasValidValue = false;

        for (Double value : values) {
            if (value != null) {
                sum += value;
                hasValidValue = true;
            }
        }

        return hasValidValue ? sum : 0.0; // ✅ If all values are null, return 0.0
    }

    private Double sumIfNotNull(Double value) {
        return value != null ? value : null; // ✅ Don't replace with 0.0
    }
}
