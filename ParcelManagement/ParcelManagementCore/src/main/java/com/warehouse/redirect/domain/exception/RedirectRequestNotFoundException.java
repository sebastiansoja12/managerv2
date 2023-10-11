package com.warehouse.redirect.domain.exception;

import com.warehouse.exception.RestException;

public class RedirectRequestNotFoundException extends RestException {
    public RedirectRequestNotFoundException(int code, String exMessage) {
        super(code, exMessage);
    }
}
