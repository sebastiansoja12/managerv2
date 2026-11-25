package com.warehouse.billing.payment.domain.exception;

public class PaymentNotReadyException extends RuntimeException {

    public PaymentNotReadyException(final String message) {
        super(message);
    }
}
