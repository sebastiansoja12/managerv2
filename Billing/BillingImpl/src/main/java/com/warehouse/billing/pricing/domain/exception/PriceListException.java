package com.warehouse.billing.pricing.domain.exception;

public class PriceListException extends RuntimeException {

    public enum Reason { INVALID_REQUEST, NOT_FOUND }

    private final Reason reason;

    public PriceListException(final Reason reason, final String message) {
        super(message);
        this.reason = reason;
    }

    public Reason reason() {
        return reason;
    }
}
