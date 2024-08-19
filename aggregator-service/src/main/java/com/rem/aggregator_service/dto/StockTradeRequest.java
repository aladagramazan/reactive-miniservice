package com.rem.aggregator_service.dto;


import com.rem.aggregator_service.domain.Ticker;
import com.rem.aggregator_service.domain.TradeAction;

public record StockTradeRequest (Ticker ticker,
                                 Integer price,
                                 Integer quantity,
                                 TradeAction action) {
}
