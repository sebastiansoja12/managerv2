package com.warehouse.deliverynetwork.domain.exception;

import com.warehouse.commonassets.identificator.DepartmentId;

public class UnavailableDepartmentException extends RuntimeException {

    public UnavailableDepartmentException(final DepartmentId departmentId) {
        super("Department is not available for delivery network connections: " + departmentId.getValue());
    }
}
