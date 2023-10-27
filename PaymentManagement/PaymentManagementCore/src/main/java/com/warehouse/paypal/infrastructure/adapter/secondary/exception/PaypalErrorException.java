package com.warehouse.paypal.infrastructure.adapter.secondary.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class PaypalErrorException extends RestException {
    public PaypalErrorException(int code, String exMessage) {
        super(code, exMessage);
    }
}
