package com.warehouse.deliveryreturn.domain.exception;

public class DeliveryRequestException extends RuntimeException {
    public DeliveryRequestException(String exMessage) {
        super(exMessage);
    }
}
