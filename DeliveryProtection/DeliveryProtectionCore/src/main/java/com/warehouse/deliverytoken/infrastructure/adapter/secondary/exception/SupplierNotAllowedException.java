package com.warehouse.deliverytoken.infrastructure.adapter.secondary.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class SupplierNotAllowedException extends RestException {


    private static final int ERROR_CODE = 9001;
    private static final String ERROR_MESSAGE_TEMPLATE = "Supplier %s does not have any access to deliveries";

    public SupplierNotAllowedException(String entityName) {
        super(ERROR_CODE, String.format(ERROR_MESSAGE_TEMPLATE, entityName));
    }
}
