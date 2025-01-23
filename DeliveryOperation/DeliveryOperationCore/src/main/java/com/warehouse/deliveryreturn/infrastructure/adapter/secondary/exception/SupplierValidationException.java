package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.exception;

public class SupplierValidationException extends BusinessException {
    public SupplierValidationException(Integer code, String msg) {
        super(code, msg);
    }
}
