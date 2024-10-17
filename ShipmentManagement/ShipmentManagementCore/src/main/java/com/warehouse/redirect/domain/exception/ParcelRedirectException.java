package com.warehouse.redirect.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class ParcelRedirectException extends RestException {
    public ParcelRedirectException(int code, String exMessage) {
        super(code, exMessage);
    }
}
