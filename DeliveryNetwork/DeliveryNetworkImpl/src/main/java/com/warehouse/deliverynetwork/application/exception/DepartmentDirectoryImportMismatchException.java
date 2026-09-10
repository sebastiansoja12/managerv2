package com.warehouse.deliverynetwork.application.exception;

import com.warehouse.commonassets.identificator.DepartmentCode;

public class DepartmentDirectoryImportMismatchException extends RuntimeException {

    public DepartmentDirectoryImportMismatchException(final DepartmentCode departmentCode) {
        super("Department type or status differs from the operator directory: " + departmentCode.getValue());
    }
}
