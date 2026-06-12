package com.warehouse.tracking.infrastructure.adapter.secondary.inpost;

import com.warehouse.tracking.domain.exception.TrackingErrorCode;

public final class InPostRequestException extends RuntimeException {

    private final TrackingErrorCode errorCode;
    private final int applicationStatus;
    private final boolean retryable;

    InPostRequestException(final TrackingErrorCode errorCode,
                           final int applicationStatus,
                           final String message,
                           final boolean retryable) {
        super(message);
        this.errorCode = errorCode;
        this.applicationStatus = applicationStatus;
        this.retryable = retryable;
    }

    TrackingErrorCode getErrorCode() {
        return errorCode;
    }

    int getApplicationStatus() {
        return applicationStatus;
    }

    public boolean isRetryable() {
        return retryable;
    }
}
