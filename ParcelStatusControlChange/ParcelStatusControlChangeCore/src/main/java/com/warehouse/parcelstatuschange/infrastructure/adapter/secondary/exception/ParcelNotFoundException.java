package com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class ParcelNotFoundException extends RestException {

    public ParcelNotFoundException(int code, String exMessage) {
        super(code, exMessage);
    }
}
