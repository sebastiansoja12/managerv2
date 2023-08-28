package com.warehouse.supplier.infrastructure.adapter.secondary.exception;

public class SupplierNotFoundException extends RuntimeException {
    public SupplierNotFoundException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public SupplierNotFoundException(String exMessage) {
        super(exMessage);
    }
}
