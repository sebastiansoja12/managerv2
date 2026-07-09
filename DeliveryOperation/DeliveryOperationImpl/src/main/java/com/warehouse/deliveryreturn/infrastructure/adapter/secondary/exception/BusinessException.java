package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class BusinessException extends RestException {
    public BusinessException(Integer code, String msg) {
        super(code, msg);
    }
}
