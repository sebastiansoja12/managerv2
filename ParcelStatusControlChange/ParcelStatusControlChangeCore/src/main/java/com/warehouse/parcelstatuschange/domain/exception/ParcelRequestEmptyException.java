package com.warehouse.parcelstatuschange.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class ParcelRequestEmptyException extends RestException {
    public ParcelRequestEmptyException(int code, String exMessage) {
        super(code, exMessage);
    }
}
