package com.warehouse.billing.invoice.domain.exception;

public class InvoiceException extends RuntimeException {

    public enum Reason { INVALID_REQUEST, NOT_FOUND, ILLEGAL_STATE }

    private final Reason reason;

    public InvoiceException(final Reason reason, final String message) {
        super(message);
        this.reason = reason;
    }

    public Reason reason() {
        return reason;
    }
}
