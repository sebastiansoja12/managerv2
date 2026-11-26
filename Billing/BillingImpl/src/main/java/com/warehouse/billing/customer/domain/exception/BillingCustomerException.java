package com.warehouse.billing.customer.domain.exception;

public class BillingCustomerException extends RuntimeException {

    public enum Reason { NOT_FOUND, DUPLICATE, NOT_ACTIVE }

    private final Reason reason;

    public BillingCustomerException(final Reason reason, final String message) {
        super(message);
        this.reason = reason;
    }

    public Reason reason() {
        return reason;
    }
}
