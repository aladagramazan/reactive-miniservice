package com.rem.aggregator_service.customer_service.dto;


import com.rem.aggregator_service.customer_service.domain.Ticker;

public record Holding(Ticker ticker,
                      Integer quantity) {
}
