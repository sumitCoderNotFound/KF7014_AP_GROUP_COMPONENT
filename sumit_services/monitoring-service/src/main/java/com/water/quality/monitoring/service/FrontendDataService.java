package com.water.quality.monitoring.service;


import com.water.quality.monitoring.dto.AveragesDTO;
import com.water.quality.monitoring.dto.StatusDTO;
import com.water.quality.monitoring.model.WaterQualityRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FrontendDataService {

    private final MonitoringService monitoringService;

    @Autowired
    public FrontendDataService(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    public StatusDTO fetchLatestStatus() {
        WaterQualityRecord latest = monitoringService.getLatestRecord();
        if (latest == null) return null;

        String flag = isSafe(latest) ? "GREEN" : "RED";
        return new StatusDTO(ZonedDateTime.now(), flag);
    }

    public AveragesDTO fetchMonthlyAverages(String month) {
        Map<String, Double> averages = computeAllMonthlyAverages(month);
        return new AveragesDTO(month, averages);
    }

    public Double fetchMonthlyAverageForParameter(String parameter, String month) {
        return computeMonthlyAverageForParameter(parameter, month);
    }

    public AveragesDTO fetchOverallAverages() {
        Map<String, Double> averages = computeAllOverallAverages();
        return new AveragesDTO("overall", averages);
    }

    public Double fetchOverallAverageForParameter(String parameter) {
        return computeOverallAverageForParameter(parameter);
    }


    public Double computeMonthlyAverageForParameter(String parameter, String month) {
        List<WaterQualityRecord> records = monitoringService.getAllRecords();
        if (records.isEmpty()) return null;

        double total = 0;
        int count = 0;

        for (WaterQualityRecord record : records) {
            if (record.getTimestamp() == null) continue;

            String recordMonth = record.getTimestamp().getYear() + "-" +
                    String.format("%02d", record.getTimestamp().getMonthValue());

            if (!recordMonth.equals(month)) continue;

            Double value = switch (parameter.toLowerCase()) {
                case "ph" -> record.getPH();
                case "alkalinity" -> record.getAlkalinity();
                case "conductivity" -> record.getConductivity();
                case "bod" -> record.getBod();
                case "nitrite" -> record.getNitrite();
                case "coppermgl" -> record.getCopperDissolved1();
                case "copperugl" -> record.getCopperDissolved2();
                case "ironugl" -> record.getIronDissolved();
                case "zincugl" -> record.getZincDissolved();
                case "tds" -> calculateTDS(record);
                default -> null;
            };

            if (value != null) {
                total += value;
                count++;
            }
        }

        return count > 0 ? total / count : null;
    }

    public Map<String, Double> computeAllMonthlyAverages(String month) {
        Map<String, Double> averages = new HashMap<>();
        averages.put("pH", computeMonthlyAverageForParameter("pH", month));
        averages.put("alkalinity", computeMonthlyAverageForParameter("alkalinity", month));
        averages.put("conductivity", computeMonthlyAverageForParameter("conductivity", month));
        averages.put("bod", computeMonthlyAverageForParameter("bod", month));
        averages.put("nitrite", computeMonthlyAverageForParameter("nitrite", month));
        averages.put("copperMgL", computeMonthlyAverageForParameter("copperMgL", month));
        averages.put("copperUgL", computeMonthlyAverageForParameter("copperUgL", month));
        averages.put("ironUgL", computeMonthlyAverageForParameter("ironUgL", month));
        averages.put("zincUgL", computeMonthlyAverageForParameter("zincUgL", month));
        averages.put("tds", computeMonthlyAverageForParameter("tds", month));
        return averages;
    }

    public Double computeOverallAverageForParameter(String parameter) {
        List<WaterQualityRecord> records = monitoringService.getAllRecords();
        if (records.isEmpty()) return null;

        double total = 0;
        int count = 0;

        for (WaterQualityRecord record : records) {
            Double value = switch (parameter.toLowerCase()) {
                case "ph" -> record.getPH();
                case "alkalinity" -> record.getAlkalinity();
                case "conductivity" -> record.getConductivity();
                case "bod" -> record.getBod();
                case "nitrite" -> record.getNitrite();
                case "coppermgl" -> record.getCopperDissolved1();
                case "copperugl" -> record.getCopperDissolved2();
                case "ironugl" -> record.getIronDissolved();
                case "zincugl" -> record.getIronDissolved();
                case "tds" -> calculateTDS(record);
                default -> null;
            };

            if (value != null) {
                total += value;
                count++;
            }
        }

        return count > 0 ? total / count : null;
    }

    public Map<String, Double> computeAllOverallAverages() {
        Map<String, Double> averages = new HashMap<>();
        averages.put("pH", computeOverallAverageForParameter("pH"));
        averages.put("alkalinity", computeOverallAverageForParameter("alkalinity"));
        averages.put("conductivity", computeOverallAverageForParameter("conductivity"));
        averages.put("bod", computeOverallAverageForParameter("bod"));
        averages.put("nitrite", computeOverallAverageForParameter("nitrite"));
        averages.put("copperMgL", computeOverallAverageForParameter("copperMgL"));
        averages.put("copperUgL", computeOverallAverageForParameter("copperUgL"));
        averages.put("ironUgL", computeOverallAverageForParameter("ironUgL"));
        averages.put("zincUgL", computeOverallAverageForParameter("zincUgL"));
        averages.put("tds", computeOverallAverageForParameter("tds")); // optional
        return averages;
    }


    private boolean isSafe(WaterQualityRecord data) {
        return (data.getPH() != null && data.getPH() >= 6.5 && data.getPH() <= 8.5) &&
                (data.getAlkalinity() != null && data.getAlkalinity() <= 500) &&
                (data.getConductivity() != null && data.getConductivity() <= 2000) &&
                (data.getNitrite() != null && data.getNitrite() < 1.0) &&
                (calculateTDS(data) <= 1000);
    }

    private double calculateTDS(WaterQualityRecord data) {
        double tds = 0;
        if (data.getCopperDissolved1() != null) tds += data.getCopperDissolved1();
        if (data.getCopperDissolved2() != null) tds += data.getCopperDissolved2() * 0.001;
        if (data.getIronDissolved() != null) tds += data.getIronDissolved() * 0.001;
        if (data.getZincDissolved() != null) tds += data.getZincDissolved() * 0.001;
        return tds;
    }
}