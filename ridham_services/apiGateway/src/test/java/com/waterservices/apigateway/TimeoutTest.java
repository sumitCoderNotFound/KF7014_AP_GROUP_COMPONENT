package com.waterservices.apigateway;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import java.util.Map;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TimeoutTest {

    private WireMockServer wireMockServer;
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8089)); // Custom port
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);

        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(clientHttpRequestFactoryWithTimeouts());
    }

    @AfterEach
    void teardown() {
        wireMockServer.stop();
    }

    @Test
    void testMonitoringTimeoutTriggersFallback() {

        stubFor(get(urlEqualTo("/api/monitoring/records/latest"))
                .willReturn(aResponse()
                        .withFixedDelay(3000) // Delay 3 seconds
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"data\": \"sample\" }")));

        ResourceAccessException exception = assertThrows(ResourceAccessException.class, () -> {
            restTemplate.exchange(
                    "http://localhost:8089/api/monitoring/records/latest",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
        });

        assertThat(exception.getMessage()).contains("Read timed out");
        System.out.println("Timeout simulated correctly: " + exception.getMessage());
    }

    private org.springframework.http.client.ClientHttpRequestFactory clientHttpRequestFactoryWithTimeouts() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(2000);
        return factory;
    }
}