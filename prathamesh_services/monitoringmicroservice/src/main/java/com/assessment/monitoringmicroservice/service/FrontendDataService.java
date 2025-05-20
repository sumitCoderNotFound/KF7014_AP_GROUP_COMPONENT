package com.assessment.monitoringmicroservice.service;

import com.assessment.monitoringmicroservice.dto.AveragesDTO;
import com.assessment.monitoringmicroservice.dto.StatusDTO;
import com.assessment.monitoringmicroservice.model.WaterReading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FrontendDataService {

    private final WaterReadingService monitoringService;

    @Autowired
    public FrontendDataService(WaterReadingService monitoringService) {
        this.monitoringService = monitoringService;
    }

    public StatusDTO fetchLatestStatus() {
        WaterReading latest = monitoringService.getLatestRecord();
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
        List<WaterReading> records = monitoringService.getAllRecords();
        if (records.isEmpty()) return null;

        double total = 0;
        int count = 0;

        for (WaterReading record : records) {
            if (record.getTimestamp() == null) continue;

            String recordMonth = record.getTimestamp().getYear() + "-" +
                    String.format("%02d", record.getTimestamp().getMonthValue());

            if (!recordMonth.equals(month)) continue;

            Double value = switch (parameter.toLowerCase()) {
                case "ph" -> record.getPhph();
                case "alkalinity" -> record.getAlkmgl();
                case "conductivity" -> record.getConduscm();
                case "bod" -> record.getBodmgl();
                case "nitrite" -> record.getNo2nmgl();
                case "coppermgl" -> record.getCusol1mgl();
                case "copperugl" -> record.getCusol2ugl();
                case "ironugl" -> record.getFesol1ugl();
                case "zincugl" -> record.getFesol1ugl();
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
        List<WaterReading> records = monitoringService.getAllRecords();
        if (records.isEmpty()) return null;

        double total = 0;
        int count = 0;

        for (WaterReading record : records) {
            Double value = switch (parameter.toLowerCase()) {
                case "ph" -> record.getPhph();
                case "alkalinity" -> record.getAlkmgl();
                case "conductivity" -> record.getConduscm();
                case "bod" -> record.getBodmgl();
                case "nitrite" -> record.getNo2nmgl();
                case "coppermgl" -> record.getCusol1mgl();
                case "copperugl" -> record.getCusol2ugl();
                case "ironugl" -> record.getFesol1ugl();
                case "zincugl" -> record.getZnsolugl();
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


    private boolean isSafe(WaterReading data) {
        return (data.getPhph() != null && data.getPhph() >= 6.5 && data.getPhph() <= 8.5) &&
                (data.getAlkmgl() != null && data.getAlkmgl() <= 500) &&
                (data.getConduscm() != null && data.getConduscm() <= 2000) &&
                (data.getNo2nmgl() != null && data.getNo2nmgl() < 1.0) &&
                (calculateTDS(data) <= 1000);
    }

    private double calculateTDS(WaterReading data) {
        double tds = 0;
        if (data.getCusol1mgl() != null) tds += data.getCusol1mgl();
        if (data.getCusol2ugl() != null) tds += data.getCusol2ugl() * 0.001;
        if (data.getFesol1ugl() != null) tds += data.getFesol1ugl() * 0.001;
        if (data.getZnsolugl() != null) tds += data.getZnsolugl() * 0.001;
        return tds;
    }
}