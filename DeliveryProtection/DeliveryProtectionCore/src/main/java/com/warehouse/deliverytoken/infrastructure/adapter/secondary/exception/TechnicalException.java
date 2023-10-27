package com.warehouse.deliverytoken.infrastructure.adapter.secondary.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class TechnicalException extends RestException {
    private static final int ERROR_CODE = 8002;
    private static final String ERROR_MESSAGE_TEMPLATE = "Http exception while connecting with %s service";

    public TechnicalException(String entityName) {
        super(ERROR_CODE, String.format(ERROR_MESSAGE_TEMPLATE, entityName));
    }
}
