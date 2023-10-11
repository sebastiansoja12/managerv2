package com.warehouse.redirect.domain.exception;

import com.warehouse.exception.RestException;

public class EmptyEmailException extends RestException {
    public EmptyEmailException(int code, String exMessage) {
        super(code, exMessage);
    }
}
