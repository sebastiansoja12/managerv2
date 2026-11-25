package com.warehouse.billing.payment.domain.exception;

public class PaymentException extends RuntimeException {

    public enum Reason { INVALID_REQUEST, NOT_FOUND, ILLEGAL_STATE, GATEWAY_FAILURE }

    private final Reason reason;

    public PaymentException(final Reason reason, final String message) {
        super(message);
        this.reason = reason;
    }

    public Reason reason() {
        return reason;
    }
}
