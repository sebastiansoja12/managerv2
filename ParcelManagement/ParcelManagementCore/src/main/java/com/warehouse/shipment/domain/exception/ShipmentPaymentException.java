package com.warehouse.shipment.domain.exception;

import com.warehouse.exception.RestException;
import com.warehouse.shipment.domain.exception.enumeration.ShipmentExceptionCodes;

public class ShipmentPaymentException extends RestException {

    public ShipmentPaymentException(ShipmentExceptionCodes code) {
        super(code.getCode(), code.getMessage());
    }
}
