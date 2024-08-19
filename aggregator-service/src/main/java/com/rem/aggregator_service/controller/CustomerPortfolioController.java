package com.rem.aggregator_service.controller;

import com.rem.aggregator_service.dto.CustomerInformation;
import com.rem.aggregator_service.dto.StockTradeResponse;
import com.rem.aggregator_service.dto.TradeRequest;
import com.rem.aggregator_service.service.CustomerPortfolioService;
import com.rem.aggregator_service.validator.RequestValidator;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
public class CustomerPortfolioController {

    private final CustomerPortfolioService customerPortfolioService;

    public CustomerPortfolioController(CustomerPortfolioService customerPortfolioService) {
        this.customerPortfolioService = customerPortfolioService;
    }

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomerInformation(@PathVariable Integer customerId) {
        return this.customerPortfolioService.getCustomerInformation(customerId);
    }

    @PostMapping("/{customerId}/trade")
    public Mono<StockTradeResponse> tradeStock(@PathVariable Integer customerId, @RequestBody Mono<TradeRequest> mono) {
        return mono.transform(RequestValidator.validate())
                .flatMap(tradeRequest -> this.customerPortfolioService.tradeStock(customerId, tradeRequest));
    }
}
