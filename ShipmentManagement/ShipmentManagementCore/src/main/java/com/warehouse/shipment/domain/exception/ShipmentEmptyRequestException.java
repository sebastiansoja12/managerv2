package com.warehouse.shipment.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;

public class ShipmentEmptyRequestException extends RestException {
    public ShipmentEmptyRequestException(ErrorCode code) {
        super(code.getCode(), code.getMessage());
    }
}