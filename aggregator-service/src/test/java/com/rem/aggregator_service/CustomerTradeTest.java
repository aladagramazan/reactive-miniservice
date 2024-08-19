package com.rem.aggregator_service;

import com.rem.aggregator_service.domain.Ticker;
import com.rem.aggregator_service.domain.TradeAction;
import com.rem.aggregator_service.dto.TradeRequest;
import org.junit.jupiter.api.Test;
import org.mockserver.model.MediaType;
import org.mockserver.model.RegexBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class CustomerTradeTest extends AbstractIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerTradeTest.class);

    @Test
    public void tradeSuccess() {
        mockCustomerTrade("customer-service/customer-trade-200.json", 200);
        var tradeRequest = new TradeRequest(Ticker.GOOGLE, TradeAction.BUY, 2);
        postTrade(tradeRequest, HttpStatus.OK)
                .jsonPath("$.customerId").isEqualTo(1)
                .jsonPath("$.ticker").isEqualTo("GOOGLE")
                .jsonPath("$.price").isEqualTo(110)
                .jsonPath("$.quantity").isEqualTo(2)
                .jsonPath("$.action").isEqualTo("BUY")
                .jsonPath("$.totalPrice").isEqualTo(220)
                .jsonPath("$.balance").isEqualTo(9780);
    }

    @Test
    public void tradeFailure() {
        mockCustomerTrade("customer-service/customer-trade-400.json", 400);
        var tradeRequest = new TradeRequest(Ticker.GOOGLE, TradeAction.BUY, 2);
        postTrade(tradeRequest, HttpStatus.BAD_REQUEST)
                .jsonPath("$.detail").isEqualTo("Customer [id=1] does not have enough funds to complete the transaction")
                .jsonPath("$.title").isEqualTo("Invalid Trade Request");
    }

    @Test
    public void inputValidation() {
        var missingTicker = new TradeRequest(null, TradeAction.BUY, 1);
        postTrade(missingTicker, HttpStatus.BAD_REQUEST)
                .jsonPath("$.detail").isEqualTo("Ticker is required")
                .jsonPath("$.title").isEqualTo("Invalid Trade Request");

        var missingAction = new TradeRequest(Ticker.GOOGLE, null, 1);
        postTrade(missingAction, HttpStatus.BAD_REQUEST)
                .jsonPath("$.detail").isEqualTo("Trade action is required")
                .jsonPath("$.title").isEqualTo("Invalid Trade Request");

        var invalidQuantity = new TradeRequest(Ticker.GOOGLE, TradeAction.BUY, 0);
        postTrade(invalidQuantity, HttpStatus.BAD_REQUEST)
                .jsonPath("$.detail").isEqualTo("Quantity should be greater than 0")
                .jsonPath("$.title").isEqualTo("Invalid Trade Request");
    }

    private void mockCustomerTrade(String path, int responseCode) {
        var stockResponseBody = resourceToString("stock-service/stock-price-200.json");
        mockServerClient
                .when(request("/stock/GOOGLE")
                )
                .respond(response(stockResponseBody)
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                );

        var customerResponseBody = resourceToString(path);
        mockServerClient
                .when(request("/customers/1/trade")
                        .withMethod("POST")
                        .withBody(RegexBody.regex(".*\"price\":110.*"))
                ).respond(response(customerResponseBody)
                        .withStatusCode(responseCode)
                        .withContentType(MediaType.APPLICATION_JSON)
                );
    }

    private WebTestClient.BodyContentSpec postTrade(TradeRequest request, HttpStatus expectedStatus) {
        return this.client
                .post()
                .uri("/customers/1/trade")
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody()
                .consumeWith(response -> log.info("Response: {}", new String(Objects.requireNonNull(response.getResponseBody()))));
    }
}
