package com.rem.aggregator_service.customer_service.exceptions;

public class InsufficientSharesException extends RuntimeException {

    private static final String MESSAGE = "Customer [id=%d] does not have enough shares to complete the transaction";

    public InsufficientSharesException(Integer customerId){
        super(MESSAGE.formatted(customerId));
    }

}
