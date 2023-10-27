package com.warehouse.shipment.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.domain.exception.enumeration.ShipmentExceptionCodes;

public class DestinationDepotDeterminationException extends RestException {

    public DestinationDepotDeterminationException(ShipmentExceptionCodes code) {
        super(code.getCode(), code.getMessage());
    }
}
