package com.rem.aggregator_service.customer_service.exceptions;

public class InsufficientBalanceException extends RuntimeException {
    private static final String MESSAGE = "Customer [id=%d] does not have enough funds to complete the transaction";

    public InsufficientBalanceException(Integer customerId){
        super(MESSAGE.formatted(customerId));
    }
}
