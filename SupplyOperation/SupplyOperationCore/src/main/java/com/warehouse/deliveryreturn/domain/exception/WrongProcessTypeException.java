package com.warehouse.deliveryreturn.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class WrongProcessTypeException extends RestException {
    public WrongProcessTypeException(int code, String exMessage) {
        super(code, exMessage);
    }
}
