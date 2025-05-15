package com.waterservices.monitoring;

import com.waterservices.monitoring.model.WaterQuality;
import com.waterservices.monitoring.repository.MonitoringRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
class MonitoringRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MonitoringRepository monitoringRepository;

    private WaterQuality waterQuality;

    @BeforeEach
    void setup() {
        waterQuality = new WaterQuality();
        waterQuality.setObjectId(123L);
        waterQuality.setpH(7.0);
        waterQuality.setTimestamp(LocalDateTime.now());
    }

    @Test
    void findByObjectId_whenRecordExists_returnsRecord() {
        entityManager.persistAndFlush(waterQuality);

        Optional<WaterQuality> found = monitoringRepository.findByObjectId(123L);

        assertThat(found).isPresent();
        assertThat(found.get().getObjectId()).isEqualTo(123L);
    }

    @Test
    void findByObjectId_whenRecordDoesNotExist_returnsEmpty() {
        Optional<WaterQuality> found = monitoringRepository.findByObjectId(999L);

        assertThat(found).isNotPresent();
    }

    @Test
    void findFirstByOrderByObjectIdDesc_returnsLatestRecord() {
        WaterQuality older = new WaterQuality();
        older.setObjectId(1L);
        older.setpH(6.5);
        older.setTimestamp(LocalDateTime.now().minusDays(1));
        entityManager.persist(older);

        WaterQuality newer = new WaterQuality();
        newer.setObjectId(2L);
        newer.setpH(7.5);
        newer.setTimestamp(LocalDateTime.now());
        entityManager.persist(newer);

        entityManager.flush();

        WaterQuality latest = monitoringRepository.findFirstByOrderByObjectIdDesc();

        assertThat(latest).isNotNull();
        assertThat(latest.getObjectId()).isEqualTo(2L);
    }

    @Test
    void findFirstByOrderByObjectIdDesc_whenEmpty_returnsNull() {
        WaterQuality latest = monitoringRepository.findFirstByOrderByObjectIdDesc();
        assertThat(latest).isNull();
    }
}