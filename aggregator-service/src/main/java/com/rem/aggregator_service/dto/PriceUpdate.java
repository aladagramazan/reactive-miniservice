package com.rem.aggregator_service.dto;

import com.rem.aggregator_service.domain.Ticker;

import java.time.LocalDateTime;

public record PriceUpdate(Ticker ticker,
                          Integer price,
                          LocalDateTime time) {
}
