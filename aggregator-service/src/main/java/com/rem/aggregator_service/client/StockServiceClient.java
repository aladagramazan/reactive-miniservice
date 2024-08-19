package com.rem.aggregator_service.client;

import com.rem.aggregator_service.domain.Ticker;
import com.rem.aggregator_service.dto.PriceUpdate;
import com.rem.aggregator_service.dto.StockPriceResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Objects;

@RequiredArgsConstructor
public class StockServiceClient {

    private static final Logger log = LoggerFactory.getLogger(StockServiceClient.class);

    private final WebClient client;
    private Flux<PriceUpdate> flux;

    public Mono<StockPriceResponse> getStockPrice(Ticker ticker) {
        return client.get()
                .uri("/stock/{ticker}", ticker)
                .retrieve()
                .bodyToMono(StockPriceResponse.class)
                .doOnNext(stockPriceResponse -> log.info("Received stock price response: {}", stockPriceResponse));
    }

    public Flux<PriceUpdate> priceUpdatesStream() {
        if (Objects.isNull(flux)) {
            this.flux = this.priceUpdates();
        }
        return this.flux;
    }

    private Flux<PriceUpdate> priceUpdates() {  // hot publisher
        return client.get()
                .uri("/stock/price-stream")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(PriceUpdate.class)
                .retryWhen(retry())
                .cache(1); // cache last value
    }

    private Retry retry() {
        return Retry.fixedDelay(100, Duration.ofSeconds(1))
                .doBeforeRetry(rs -> log.error("stoce service price stream call failed retrying: {}", rs.failure().getMessage()));
    }
}
