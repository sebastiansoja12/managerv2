package com.warehouse.deliverytoken.infrastructure.adapter.secondary.exception;

import com.warehouse.exception.RestException;

public class BusinessException extends RestException {
    private static final int ERROR_CODE = 8000;
    private static final String ERROR_MESSAGE_TEMPLATE = "Resource with ID: %s was not found";

    public BusinessException(String entityName) {
        super(ERROR_CODE, String.format(ERROR_MESSAGE_TEMPLATE, entityName));
    }
}
