package com.warehouse.billing.payment.infrastructure.adapter.primary.exception;

public class InvalidWebhookPayloadException extends RuntimeException {

    public InvalidWebhookPayloadException(final String message) {
        super(message);
    }
}
