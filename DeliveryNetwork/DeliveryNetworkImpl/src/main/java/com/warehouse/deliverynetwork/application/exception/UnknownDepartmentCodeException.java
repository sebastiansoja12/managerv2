package com.warehouse.deliverynetwork.application.exception;

import com.warehouse.commonassets.identificator.DepartmentCode;

public class UnknownDepartmentCodeException extends RuntimeException {

    public UnknownDepartmentCodeException(final DepartmentCode departmentCode) {
        super("Department code does not exist in the operator directory: " + departmentCode.getValue());
    }
}
