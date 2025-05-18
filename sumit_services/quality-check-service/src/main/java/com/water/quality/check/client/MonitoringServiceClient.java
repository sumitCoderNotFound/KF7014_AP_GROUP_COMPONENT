package com.water.quality.check.client;

import com.water.quality.check.model.QualityCheckRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MonitoringServiceClient {

    @Value("${monitoring.service.url}")
    private String monitoringServiceUrl;

    private final RestTemplate restTemplate;

    public MonitoringServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public QualityCheckRequest fetchLatestWaterQualityData() {
        String url = monitoringServiceUrl + "/latest";
        return restTemplate.getForObject(url, QualityCheckRequest.class);
    }
    public QualityCheckRequest fetchLatestWaterQualityData(String authHeader) {
        String url = monitoringServiceUrl + "/latest";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<QualityCheckRequest> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    QualityCheckRequest.class
            );

            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error calling monitoring service: " + e.getMessage());
            return null;
        }
    }
}
