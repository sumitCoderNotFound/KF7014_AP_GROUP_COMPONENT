package com.water.quality.check.service;

import com.water.quality.check.model.QualityCheckRequest;
import com.water.quality.check.model.QualityCheckResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QualityCheckServiceTest {

    private final QualityCheckService qualityCheckService = new QualityCheckService();

    @Test
    void validateWaterQuality_ShouldReturnGreenFlag_WhenParametersAreSafe() {
        QualityCheckRequest request = new QualityCheckRequest(7.0, 100.0, 500.0, 2.5, 0.5, 0.8, 0.7, 0.2, 3.0);
        QualityCheckResponse response = qualityCheckService.validateWaterQuality(request);

        assertNotNull(response);
        assertEquals("GREEN", response.getSafetyFlag());
        assertNotNull(response.getCalculatedTDS());
        assertTrue(response.getCalculatedTDS() > 0);
    }

    @Test
    void validateWaterQuality_ShouldReturnRedFlag_WhenParametersAreUnsafe() {
        QualityCheckRequest request = new QualityCheckRequest(9.0, 150.0, 1100.0, 5.0, 2.0, 2.0, 2.0, 1.0, 6.0);
        QualityCheckResponse response = qualityCheckService.validateWaterQuality(request);

        assertNotNull(response);
        assertEquals("RED", response.getSafetyFlag());
        assertNotNull(response.getCalculatedTDS());
        assertTrue(response.getCalculatedTDS() > 0);
    }

    @Test
    void validateWaterQuality_ShouldHandleNullValues() {
        QualityCheckRequest request = new QualityCheckRequest(null, null, null, null, null, null, null, null, null);
        QualityCheckResponse response = qualityCheckService.validateWaterQuality(request);

        assertNotNull(response);
        assertEquals("GREEN", response.getSafetyFlag()); // ✅ Treats null values as safe
        assertEquals(0.0, response.getCalculatedTDS()); // ✅ Expect TDS to be 0.0, not null
    }

}
