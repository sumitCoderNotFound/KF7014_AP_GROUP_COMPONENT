package com.example.waterquality.constants;

/**
 * Constants class defining World Health Organization (WHO) limits for drinking water quality parameters.
 * These constants represent the maximum and minimum acceptable values for various water quality indicators
 * as per WHO drinking water quality guidelines.
 */
public class WhoLimits {
    /**
     * Maximum acceptable turbidity in Nephelometric Turbidity Units (NTU)
     * as per WHO drinking water guidelines.
     */
    public static final double MAX_TURBIDITY_NTU = 5.0;
    
    /**
     * Minimum acceptable pH value for drinking water.
     * Water with pH below this value is considered acidic and potentially harmful.
     */
    public static final double MIN_PH = 6.5;
    
    /**
     * Maximum acceptable pH value for drinking water.
     * Water with pH above this value is considered alkaline and potentially harmful.
     */
    public static final double MAX_PH = 8.5;
    
    /**
     * Maximum acceptable alkalinity in milligrams per liter (mg/L).
     */
    public static final double MAX_ALKALINITY_MG_L = 500.0;
    
    /**
     * Maximum acceptable electrical conductivity in microsiemens per centimeter (µS/cm).
     */
    public static final double MAX_CONDUCTIVITY_US_CM = 2000.0;
    
    /**
     * Maximum acceptable nitrite concentration in milligrams per liter (mg/L).
     */
    public static final double MAX_NITRITE_MG_L = 1.0;
    
    /**
     * Maximum acceptable Total Dissolved Solids (TDS) in milligrams per liter (mg/L).
     */
    public static final double MAX_TDS_MG_L = 1000.0;

    /**
     * Conversion factor to convert micrograms per liter (µg/L) to milligrams per liter (mg/L).
     * Multiply µg/L values by this factor to get mg/L.
     */
    public static final double UG_L_TO_MG_L = 0.001;
} 