package com.warehouse.deliveryhandover.domain.exception;

public class EmptyDeliveryHandoverException extends RuntimeException {

    public EmptyDeliveryHandoverException(final String message) {
        super(message);
    }
}
