package com.water.quality.gateway.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ApiGatewayConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeAll
    static void setup() {
        System.out.println("🔹 Starting API Gateway Tests...");
    }

    @Test
    void testMonitoringServiceRouting() {
        System.out.println("🔍 Testing: /api/water-quality/records");

        webTestClient.get().uri("/api/water-quality/records") // ✅ Correct endpoint
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json") // ✅ Ensure JSON response
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertNotNull(response.getResponseBody(), "❌ Monitoring Service response should not be null");
                    System.out.println("✅ Monitoring Service Response: " + response.getResponseBody());
                });
    }


    @Test
    void testQualityCheckServiceRouting() {
        webTestClient.get().uri("/api/quality-check/validate")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response ->
                        assertNotNull(response.getResponseBody(), "Response should not be null"));
    }

}
