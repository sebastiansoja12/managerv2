package com.warehouse.analytics.domain.exception;

public class AnalyticsException extends RuntimeException {

    public enum Reason { INVALID_REQUEST, NOT_FOUND }

    private final Reason reason;

    public AnalyticsException(final Reason reason, final String message) {
        super(message);
        this.reason = reason;
    }

    public Reason reason() {
        return reason;
    }
}
