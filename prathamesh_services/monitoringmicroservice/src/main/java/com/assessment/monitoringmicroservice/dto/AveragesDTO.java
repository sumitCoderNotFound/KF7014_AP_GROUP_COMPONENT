package com.assessment.monitoringmicroservice.dto;

import java.util.Map;

public class AveragesDTO {
    private String month;
    private Map<String, Double> averages;

    // Constructors, getters, setters

    public AveragesDTO() {}

    public AveragesDTO(String month, Map<String, Double> averages) {
        this.month = month;
        this.averages = averages;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Map<String, Double> getAverages() {
        return averages;
    }

    public void setAverages(Map<String, Double> averages) {
        this.averages = averages;
    }
}
