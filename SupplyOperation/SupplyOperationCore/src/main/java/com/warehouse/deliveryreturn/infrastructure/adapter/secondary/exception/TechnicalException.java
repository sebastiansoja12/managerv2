package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class TechnicalException extends RestException {

    public TechnicalException(Integer code, String msg) {
        super(code, msg);
    }
}
