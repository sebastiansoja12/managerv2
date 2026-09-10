package com.warehouse.deliverynetwork.domain.exception;

import com.warehouse.commonassets.identificator.DepartmentId;

public class SelfDepartmentConnectionException extends RuntimeException {

    public SelfDepartmentConnectionException(final DepartmentId departmentId) {
        super("Department cannot be connected to itself: " + departmentId.getValue());
    }
}
