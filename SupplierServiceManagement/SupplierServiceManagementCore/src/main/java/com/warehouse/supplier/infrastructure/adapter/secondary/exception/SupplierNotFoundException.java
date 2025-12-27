package com.warehouse.supplier.infrastructure.adapter.secondary.exception;

public class SupplierNotFoundException extends TechnicalException {

    public SupplierNotFoundException(String exMessage) {
        super(exMessage);
    }
}
