package com.warehouse.redirect.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class TokenNotFoundException extends RestException {
    public TokenNotFoundException(int code, String exMessage) {
        super(code, exMessage);
    }
}
