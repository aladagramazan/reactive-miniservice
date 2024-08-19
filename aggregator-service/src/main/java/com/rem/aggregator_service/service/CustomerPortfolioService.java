package com.rem.aggregator_service.service;

import com.rem.aggregator_service.client.CustomerServiceClient;
import com.rem.aggregator_service.client.StockServiceClient;
import com.rem.aggregator_service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerPortfolioService {

    private final CustomerServiceClient customerServiceClient;
    private final StockServiceClient stockServiceClient;

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return this.customerServiceClient.getCustomerInformation(customerId);
    }

    public Mono<StockTradeResponse> tradeStock(Integer customerId, TradeRequest request) {
        return this.stockServiceClient.getStockPrice(request.ticker())
                .map(StockPriceResponse::price)
                .map(price -> this.createTradeRequest(request, price))
                .flatMap(tradeRequest -> this.customerServiceClient.tradeStock(customerId, tradeRequest));
    }

    private StockTradeRequest createTradeRequest(TradeRequest request, Integer price) {
        return new StockTradeRequest(
                request.ticker(),
                price,
                request.quantity(),
                request.action());
    }
}
