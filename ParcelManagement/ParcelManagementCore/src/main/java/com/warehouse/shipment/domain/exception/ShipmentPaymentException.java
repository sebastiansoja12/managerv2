package com.warehouse.shipment.domain.exception;

public class ShipmentPaymentException extends RuntimeException {

    public ShipmentPaymentException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public ShipmentPaymentException(String exMessage) {
        super(exMessage);
    }
}
