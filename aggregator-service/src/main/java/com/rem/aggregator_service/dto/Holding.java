package com.rem.aggregator_service.dto;


import com.rem.aggregator_service.domain.Ticker;

public record Holding(Ticker ticker,
                      Integer quantity) {
}
