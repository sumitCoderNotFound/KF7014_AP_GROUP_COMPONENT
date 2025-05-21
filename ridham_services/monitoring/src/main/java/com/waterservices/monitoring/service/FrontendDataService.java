package com.waterservices.monitoring.service;

import com.waterservices.monitoring.dto.AveragesDTO;
import com.waterservices.monitoring.dto.StatusDTO;
import com.waterservices.monitoring.model.WaterQuality;
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
        WaterQuality latest = monitoringService.getLatestWaterQualityRecord();
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
        List<WaterQuality> records = monitoringService.getAllWaterQualityRecords();
        if (records.isEmpty()) return null;

        double total = 0;
        int count = 0;

        for (WaterQuality record : records) {
            if (record.getTimestamp() == null) continue;

            String recordMonth = record.getTimestamp().getYear() + "-" +
                    String.format("%02d", record.getTimestamp().getMonthValue());

            if (!recordMonth.equals(month)) continue;

            Double value = switch (parameter.toLowerCase()) {
                case "ph" -> record.getpH();
                case "alkalinity" -> record.getAlkalinity();
                case "conductivity" -> record.getConductivity();
                case "bod" -> record.getBod();
                case "nitrite" -> record.getNitriteN();
                case "coppermgl" -> record.getCopperMgL();
                case "copperugl" -> record.getCopperUgL();
                case "ironugl" -> record.getIronUgL();
                case "zincugl" -> record.getZincUgL();
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
        List<WaterQuality> records = monitoringService.getAllWaterQualityRecords();
        if (records.isEmpty()) return null;

        double total = 0;
        int count = 0;

        for (WaterQuality record : records) {
            Double value = switch (parameter.toLowerCase()) {
                case "ph" -> record.getpH();
                case "alkalinity" -> record.getAlkalinity();
                case "conductivity" -> record.getConductivity();
                case "bod" -> record.getBod();
                case "nitrite" -> record.getNitriteN();
                case "coppermgl" -> record.getCopperMgL();
                case "copperugl" -> record.getCopperUgL();
                case "ironugl" -> record.getIronUgL();
                case "zincugl" -> record.getZincUgL();
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


    private boolean isSafe(WaterQuality data) {
        return (data.getpH() != null && data.getpH() >= 6.5 && data.getpH() <= 8.5) &&
                (data.getAlkalinity() != null && data.getAlkalinity() <= 500) &&
                (data.getConductivity() != null && data.getConductivity() <= 2000) &&
                (data.getNitriteN() != null && data.getNitriteN() < 1.0) &&
                (calculateTDS(data) <= 1000);
    }

    private double calculateTDS(WaterQuality data) {
        double tds = 0;
        if (data.getCopperMgL() != null) tds += data.getCopperMgL();
        if (data.getCopperUgL() != null) tds += data.getCopperUgL() * 0.001;
        if (data.getIronUgL() != null) tds += data.getIronUgL() * 0.001;
        if (data.getZincUgL() != null) tds += data.getZincUgL() * 0.001;
        return tds;
    }
}