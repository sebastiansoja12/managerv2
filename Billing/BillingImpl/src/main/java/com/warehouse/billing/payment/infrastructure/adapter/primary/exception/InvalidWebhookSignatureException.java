package com.warehouse.billing.payment.infrastructure.adapter.primary.exception;

public class InvalidWebhookSignatureException extends RuntimeException {

    public InvalidWebhookSignatureException(final String message) {
        super(message);
    }
}
