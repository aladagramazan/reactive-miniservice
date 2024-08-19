package com.rem.aggregator_service;

import org.junit.jupiter.api.Test;
import org.mockserver.model.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class CustomerInformationTest extends AbstractIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerInformationTest.class);

    @Test
    public void testCustomerInformation() {
        mockCustomerInformation("customer-service/customer-information-200.json", HttpStatus.OK.value());
        getCustomerInformation(HttpStatus.OK)
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Sam")
                .jsonPath("$.balance").isEqualTo(10000)
                .jsonPath("$.holdings.length()").isEqualTo(1)
                .jsonPath("$.holdings[0].ticker").isEqualTo("GOOGLE")
                .jsonPath("$.holdings[0].quantity").isEqualTo(5);
    }

    @Test
    public void testCustomerNotFound() {
        mockCustomerInformation("customer-service/customer-information-404.json", HttpStatus.NOT_FOUND.value());
        getCustomerInformation(HttpStatus.NOT_FOUND)
                .jsonPath("$.detail").isEqualTo("Customer [id=1] is not found")
                .jsonPath("$.title").isEqualTo("Customer not found");
    }

    private void mockCustomerInformation(String path, int responseCode) {
        var responseBody = resourceToString(path);
        mockServerClient
                .when(request("/customers/1")
                )
                .respond(response(responseBody)
                        .withStatusCode(responseCode)
                        .withContentType(MediaType.APPLICATION_JSON)
                );
    }

    private WebTestClient.BodyContentSpec getCustomerInformation(HttpStatus expectedStatus) {
        return this.client
                .get()
                .uri("/customers/1")
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody()
                .consumeWith(response ->
                        log.info("Response: {}", new String(Objects.requireNonNull(response.getResponseBody()))));
    }
}
