package com.warehouse.returning.infrastructure.adapter.secondary.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class BusinessException extends RestException {
    public BusinessException(final int code, final String message) {
        super(code, message);
    }
}
