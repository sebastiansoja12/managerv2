package com.warehouse.platformadministration.domain.exception;

public class OperatorDataErasureNotAllowedException extends RuntimeException {

    private final String reason;

    public OperatorDataErasureNotAllowedException(final String reason, final String message) {
        super(message);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
