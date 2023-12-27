package com.warehouse.deliverytoken.infrastructure.adapter.secondary.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class CommunicationException extends RestException {

    private static final int ERROR_CODE = 400;
    private static final String ERROR_MESSAGE_TEMPLATE = "Connection with %s could not be established";

    public CommunicationException(String entityName) {
        super(ERROR_CODE, String.format(ERROR_MESSAGE_TEMPLATE, entityName));
    }
}

