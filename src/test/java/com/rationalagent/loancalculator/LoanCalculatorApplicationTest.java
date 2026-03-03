package com.rationalagent.loancalculator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoanCalculatorApplicationTest {

    @LocalServerPort
    int randomServerPort;

    @Test
    void testHealth() throws URISyntaxException {
        var restTemplate = new RestTemplate();

        final var baseUrl = "http://localhost:" + randomServerPort + "/actuator/health";
        var uri = new URI(baseUrl);

        var result = restTemplate.getForEntity(uri, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).contains("UP");
    }
}
