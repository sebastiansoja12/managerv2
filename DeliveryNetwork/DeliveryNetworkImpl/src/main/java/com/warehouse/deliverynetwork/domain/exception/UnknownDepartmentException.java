package com.warehouse.deliverynetwork.domain.exception;

import com.warehouse.commonassets.identificator.DepartmentId;

public class UnknownDepartmentException extends RuntimeException {

    public UnknownDepartmentException(final DepartmentId departmentId) {
        super("Department does not exist in the operator directory: " + departmentId.getValue());
    }
}
