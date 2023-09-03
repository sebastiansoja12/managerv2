package com.warehouse.paypal.infrastructure.adapter.secondary.exception;

import com.warehouse.exception.RestException;

public class PaymentNotFoundException extends RestException {
    public PaymentNotFoundException(int code, String exMessage) {
        super(code, exMessage);
    }
}
