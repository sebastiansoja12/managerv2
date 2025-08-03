package com.warehouse.shipment.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.domain.exception.enumeration.ShipmentErrorCode;

public class DestinationDepartmentDeterminationException extends RestException {

    public DestinationDepartmentDeterminationException(ShipmentErrorCode code) {
        super(code.getCode(), code.getMessage());
    }
}
