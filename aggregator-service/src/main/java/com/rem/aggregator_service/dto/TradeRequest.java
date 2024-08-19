package com.rem.aggregator_service.dto;

import com.rem.aggregator_service.domain.Ticker;
import com.rem.aggregator_service.domain.TradeAction;

public record TradeRequest(Ticker ticker,
                           TradeAction action,
                           Integer quantity) {
}
