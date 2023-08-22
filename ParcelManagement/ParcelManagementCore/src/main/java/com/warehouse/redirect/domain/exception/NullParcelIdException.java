package com.warehouse.redirect.domain.exception;

import com.warehouse.exception.RestException;

public class NullParcelIdException extends RestException {
    public NullParcelIdException(int code, String exMessage) {
        super(code, exMessage);
    }
}
