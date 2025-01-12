package com.warehouse.deliverymissed.domain.exception;

import com.warehouse.deliverymissed.infrastructure.adapter.primary.dto.exception.RestException;

public class EmptyDepotCodeException extends RestException {

    private final static Integer code = 401;

    public EmptyDepotCodeException(String exMessage) {
        super(code, exMessage);
    }
}
