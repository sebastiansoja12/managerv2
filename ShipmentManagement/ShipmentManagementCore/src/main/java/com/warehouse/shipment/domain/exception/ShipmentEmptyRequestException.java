package com.warehouse.shipment.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.domain.exception.enumeration.ShipmentErrorCode;

public class ShipmentEmptyRequestException extends RestException {
    public ShipmentEmptyRequestException(ShipmentErrorCode code) {
        super(code.getCode(), code.getMessage());
    }
}