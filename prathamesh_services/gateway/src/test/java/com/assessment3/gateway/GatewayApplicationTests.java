package com.assessment3.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Unit tests for the Gateway Application. These tests the functionality of the routes in the Gateway
 * service by making HTTP requests to the endpoints.
 * {@link WebTestClient} is used to to perform the HTTP requests and response validation..
 *
 * @author Prathamesh Belnekar
 * @version 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GatewayApplicationTests {


	/**
	 * Server port used for testing and is dynamically assigned by Spring Boot.
	 */
	@LocalServerPort
	private int port;


	/**
	 * WebTestClient is used to perform HTTP requests and to verify the responses.
	 */
	@Autowired
	private WebTestClient webTestClient;


	/**
	 * Test for the /watermonitoring/records endpoint to check if it
	 * returns a successful response.
	 */
	@Test
	public void testWaterMonitoringRoutes() {

		webTestClient.get().uri("http://localhost:" + port + "/watermonitoring/records")
		.exchange()
		.expectStatus().is2xxSuccessful()
		.expectBody(String.class);
	}

	/**
	 * Test for the /waterquality/records/latestflagged endpoint to check if
	 *  it returns a successful response.
	 */
	/*@Test
	public void testWaterQualityRoutes() {

		webTestClient.get().uri("http://localhost:" + port + "/waterquality/records/latestflagged")
		.exchange()
		.expectStatus().is2xxSuccessful()
		.expectBody(String.class);
	}
*/
	/**
	 * Test for the fallback behavior of the Water Quality Service when
	 * the service is not avaliable.
	 */
	@Test
	public void testQualityServiceFallback() {
		webTestClient.get().uri("http://localhost:" + port + "/waterquality/records/latestflagged")
		.exchange()
		.expectStatus().isOk()
		.expectBody(String.class)
		.isEqualTo("Water quality microservice is currently not available. Please try again later.");
	}
}