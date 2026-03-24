package com.warehouse.exceptionhandler.exception;

import com.warehouse.commonassets.exception.ProblemDetailsException;

public class RestException extends ProblemDetailsException {

    public RestException(final int code, final String exMessage) {
        super("about:blank", "Request failed", code, exMessage, null, null);
    }

    public int getCode() {
        return getStatus();
    }
}
