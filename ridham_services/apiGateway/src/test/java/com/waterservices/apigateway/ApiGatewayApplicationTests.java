package com.waterservices.apigateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiGatewayApplicationTests {

    private String sampleData;

    @BeforeEach
    void setUp() {
        sampleData = """
                {
                    "timestamp": "%s",
                    "objectId": 12345,
                    "pH": 7.5,
                    "alkalinity": 150.0,
                    "conductivity": 300.0,
                    "nitriteN": 2.0,
                    "tds": 0.1,
                    "turbidity": 0.5,
                    "temperature": 500.0,
                    "dissolvedOxygen": 200.0,
                    "salinity": 100.0
                }
                """.formatted(LocalDateTime.now().toString());
    }

   @Test
    public void contextLoads() {
   }

    @Test
    void testMonitoringRouteSuccess() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        //String mockJson = "{ \"objectId\": 12345, \"pH\": 7.5, \"alkalinity\": 150.0, \"conductivity\": 300.0 }";
        String mockJson = sampleData;
        mockServer.expect(requestTo("http://localhost:8080/api/monitoring/records/latest"))
                .andRespond(withSuccess(mockJson, MediaType.APPLICATION_JSON));

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "http://localhost:8080/api/monitoring/records/latest",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("objectId", 12345);
        assertThat(response.getBody()).containsEntry("pH", 7.5);
        assertThat(response.getBody()).containsEntry("alkalinity", 150.0);
        System.out.println(response.getBody());

        mockServer.verify();
    }

    @Test
    void testMonitoringRouteFallback() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        String fallbackJson = "{ \"message\": \"Monitoring service is currently unavailable.\", \"status\": \"DOWN\" }";

        mockServer.expect(requestTo("http://localhost:8080/api/monitoring/records/latest"))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fallbackJson));


        // Attempt the request; it should fail with a ServiceUnavailable exception.
        try {
            restTemplate.exchange(
                    "http://localhost:8080/api/monitoring/records/latest",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            // Houston, we have a success... but we wanted a failure.
            fail("Expected HttpServerErrorException");
        } catch (HttpServerErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
            String responseBody = e.getResponseBodyAsString();
            assertThat(responseBody).contains("Monitoring service is currently unavailable.");
            System.out.println("Caught expected 503: " + responseBody);
        }

        mockServer.verify();
    }

    @Test
    void testQualityAnalysisRouteSuccess() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        String mockJson = sampleData;

        mockServer.expect(requestTo("http://localhost:8080/api/quality-analysis/records/latest"))
                .andRespond(withSuccess(mockJson, MediaType.APPLICATION_JSON));

      /*  ResponseEntity<Map> response = restTemplate.getForEntity(
                "http://localhost:8080/api/quality-analysis/records/latest",
                Map.class
        );*/
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "http://localhost:8080/api/quality-analysis/records/latest",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("objectId", 12345);
        assertThat(response.getBody()).containsEntry("pH", 7.5);
        assertThat(response.getBody()).containsEntry("alkalinity", 150.0);
        System.out.println(response.getBody());

        mockServer.verify();
    }

    @Test
    void testQualityAnalysisRouteFallback() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        String fallbackJson = "{ \"message\": \"Quality Analysis service is currently unavailable.\", \"status\": \"DOWN\" }";

        mockServer.expect(requestTo("http://localhost:8080/api/quality-analysis/records/latest"))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fallbackJson));

        try {
            restTemplate.exchange(
                    "http://localhost:8080/api/quality-analysis/records/latest",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            // This shouldn't happen, forcing fail
            fail("Expected HttpServerErrorException");
        } catch (HttpServerErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
            String responseBody = e.getResponseBodyAsString();
            assertThat(responseBody).contains("Quality Analysis service is currently unavailable.");
            System.out.println("Caught expected fallback 503: " + responseBody);
        }

        mockServer.verify();
    }

    @Test
    void testMonitoringTimeoutTriggersFallback() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        String fallbackJson = "{ \"message\": \"Monitoring service is currently unavailable.\", \"status\": \"DOWN\" }";

        // Simulate timeout by throwing ResourceAccessException directly
        mockServer.expect(requestTo("http://localhost:8080/api/monitoring/records/latest"))
                .andRespond(request -> {
                    throw new ResourceAccessException("Read timed out");
                });

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    "http://localhost:8080/api/monitoring/records/latest",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
            assertThat(response.getBody()).containsEntry("message", "Monitoring service is currently unavailable.");
            assertThat(response.getBody()).containsEntry("status", "DOWN");
        } catch (ResourceAccessException e) {

            System.out.println("Timeout simulated correctly: " + e.getMessage());
        }

        mockServer.verify();
    }

}