package com.warehouse.deliverymissed.domain.exception;

import com.warehouse.deliverymissed.infrastructure.adapter.primary.dto.exception.RestException;

public class WrongDeliveryStatusException extends RestException {

    private final static Integer code = 401;

    public WrongDeliveryStatusException(String exMessage) {
        super(code, exMessage);
    }
}
