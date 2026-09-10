package com.warehouse.organisationstructure.subscription.domain.exception;

public class InvalidSubscriptionException extends RuntimeException {

    public InvalidSubscriptionException(final String message) {
        super(message);
    }
}
