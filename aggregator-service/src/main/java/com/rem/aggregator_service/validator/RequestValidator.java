package com.rem.aggregator_service.validator;

import com.rem.aggregator_service.dto.TradeRequest;
import com.rem.aggregator_service.exceptions.ApplicationExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RequestValidator {

    public static UnaryOperator<Mono<TradeRequest>> validate() {
        return mono -> mono
                .filter(hasTicker())
                .switchIfEmpty(ApplicationExceptions.missingTicker())
                .filter(hasTradeAction())
                .switchIfEmpty(ApplicationExceptions.missingTradeAction())
                .filter(isValidQuantity())
                .switchIfEmpty(ApplicationExceptions.invalidQuantity());
    }

    private static Predicate<TradeRequest> hasTicker() {
        return tradeRequest -> Objects.nonNull(tradeRequest.ticker());
    }

    private static Predicate<TradeRequest> hasTradeAction() {
        return tradeRequest -> Objects.nonNull(tradeRequest.action());
    }

    private static Predicate<TradeRequest> isValidQuantity() {
        return tradeRequest -> Objects.nonNull(tradeRequest.quantity()) && tradeRequest.quantity() > 0;
    }
}
