package com.warehouse.supplier.domain.exception;

public class SupplierNotFoundException extends BusinessException {

    public SupplierNotFoundException(final String message) {
        super(message);
    }
}
