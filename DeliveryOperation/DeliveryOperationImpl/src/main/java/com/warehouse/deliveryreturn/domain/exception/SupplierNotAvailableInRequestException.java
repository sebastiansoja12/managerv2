package com.warehouse.deliveryreturn.domain.exception;

public class SupplierNotAvailableInRequestException extends RuntimeException {
    public SupplierNotAvailableInRequestException(String message) {
        super(message);
    }
}
