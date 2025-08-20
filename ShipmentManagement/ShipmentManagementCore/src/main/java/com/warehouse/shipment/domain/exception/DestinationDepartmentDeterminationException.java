package com.warehouse.shipment.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;

public class DestinationDepartmentDeterminationException extends RestException {

    public DestinationDepartmentDeterminationException(ErrorCode code) {
        super(code.getCode(), code.getMessage());
    }
}
