package com.warehouse.deliveryreturn.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class WrongDeliveryStatusException extends RestException {
    public WrongDeliveryStatusException(int code, String exMessage) {
        super(code, exMessage);
    }
}
