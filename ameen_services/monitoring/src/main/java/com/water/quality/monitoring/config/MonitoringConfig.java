package com.water.quality.monitoring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "monitoring")
public class MonitoringConfig {
    private String csvFilePath;
    private int readIntervalSeconds = 30;
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private String csvDelimiter = ",";

    // Getters and Setters
    public String getCsvFilePath() {
        return csvFilePath;
    }

    public void setCsvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public int getReadIntervalSeconds() {
        return readIntervalSeconds;
    }

    public void setReadIntervalSeconds(int readIntervalSeconds) {
        this.readIntervalSeconds = readIntervalSeconds;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getCsvDelimiter() {
        return csvDelimiter;
    }

    public void setCsvDelimiter(String csvDelimiter) {
        this.csvDelimiter = csvDelimiter;
    }
} 