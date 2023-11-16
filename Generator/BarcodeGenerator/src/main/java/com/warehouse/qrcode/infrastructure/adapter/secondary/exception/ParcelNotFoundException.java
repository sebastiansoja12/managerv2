package com.warehouse.qrcode.infrastructure.adapter.secondary.exception;

public class ParcelNotFoundException extends RuntimeException {
    public ParcelNotFoundException(String message) {
        super(message);
    }
}
