package com.rem.aggregator_service.dto;

import com.rem.aggregator_service.domain.Ticker;

public record StockPriceResponse(Ticker ticker,
                                 Integer price) {
}
