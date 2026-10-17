package com.warehouse.deliverycollect.domain.exception;

public class EmptyDeliveryCollectException extends RuntimeException {
    public EmptyDeliveryCollectException(final String message) {
        super(message);
    }
}
