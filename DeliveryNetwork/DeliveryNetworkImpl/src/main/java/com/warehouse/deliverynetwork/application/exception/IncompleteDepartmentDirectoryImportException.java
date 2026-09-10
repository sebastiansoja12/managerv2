package com.warehouse.deliverynetwork.application.exception;

import com.warehouse.commonassets.identificator.DepartmentCode;

import java.util.List;

public class IncompleteDepartmentDirectoryImportException extends RuntimeException {

    public IncompleteDepartmentDirectoryImportException(final List<DepartmentCode> missingDepartmentCodes) {
        super("The import does not contain all operator departments: " + missingDepartmentCodes);
    }
}
