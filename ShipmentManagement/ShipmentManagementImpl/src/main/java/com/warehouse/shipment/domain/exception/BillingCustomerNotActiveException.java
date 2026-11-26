package com.warehouse.shipment.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;

public class BillingCustomerNotActiveException extends RestException {

    public BillingCustomerNotActiveException() {
        super(ErrorCode.BILLING_CUSTOMER_NOT_ACTIVE.getCode(), ErrorCode.BILLING_CUSTOMER_NOT_ACTIVE.getMessage());
    }
}
