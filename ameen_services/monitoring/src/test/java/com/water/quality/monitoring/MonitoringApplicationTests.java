package com.water.quality.monitoring;

import com.water.quality.monitoring.config.TestConfig;
import com.water.quality.monitoring.repository.WaterMonitorRepository;
import com.water.quality.monitoring.service.MonitorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@SpringBootTest(properties = {
        "spring.main.allow-bean-definition-overriding=true",
        "spring.task.scheduling.enabled=false"
})
@Import(TestConfig.class)
@TestPropertySource(properties = {
        "monitoring.csvFilePath=test.csv",
        "monitoring.readIntervalSeconds=3600",
        "monitoring.dateFormat=yyyy-MM-dd HH:mm:ss",
        "monitoring.csvDelimiter=,"
})
class MonitoringApplicationTests {


    @Autowired
    private MonitorService monitorService;

    @MockitoBean
    private WaterMonitorRepository repository;

    @Test
    void contextLoads() {
        // Verify that the service and repository are properly initialized
        assert monitorService != null;
        assert repository != null;
    }
}
