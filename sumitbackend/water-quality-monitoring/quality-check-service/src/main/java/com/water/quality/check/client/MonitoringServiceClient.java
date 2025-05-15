package com.water.quality.check.client;

import com.water.quality.check.model.QualityCheckRequest;
import org.springframework.beans.factory.annotation.Value;
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

}
