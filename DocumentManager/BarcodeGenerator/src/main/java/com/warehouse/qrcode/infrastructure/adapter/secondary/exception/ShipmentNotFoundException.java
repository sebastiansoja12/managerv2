package com.warehouse.qrcode.infrastructure.adapter.secondary.exception;

public class ShipmentNotFoundException extends RuntimeException {
    public ShipmentNotFoundException(String message) {
        super(message);
    }
}
