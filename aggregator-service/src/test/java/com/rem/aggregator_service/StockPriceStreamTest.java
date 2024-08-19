package com.rem.aggregator_service;

import com.rem.aggregator_service.dto.PriceUpdate;
import org.junit.jupiter.api.Test;
import org.mockserver.model.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class StockPriceStreamTest extends AbstractIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(StockPriceStreamTest.class);

    @Test
    public void priceStream() {
        var responseBody = resourceToString("stock-service/stock-price-stream-200.json");
        mockServerClient
                .when(request("/stock/price-stream")
                )
                .respond(response(responseBody)
                        .withStatusCode(200)
                        .withContentType(MediaType.parse("application/x-ndjson"))
                );

        this.client.get()
                .uri("/stock/price-stream")
                .accept(org.springframework.http.MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(PriceUpdate.class)
                .getResponseBody()
                .doOnNext(priceUpdate -> log.info("Received price update: {}", priceUpdate))
                .as(StepVerifier::create)
                .assertNext(priceUpdate -> assertEquals(53, priceUpdate.price()))
                .assertNext(priceUpdate -> assertEquals(54, priceUpdate.price()))
                .assertNext(priceUpdate -> assertEquals(55, priceUpdate.price()))
                .expectComplete()
                .verify();
    }
}
