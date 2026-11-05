package com.warehouse.organisationstructure.operatorconfiguration.domain.exception;

import com.warehouse.commonassets.identificator.DepartmentId;

public class DepartmentConfigurationAccessDeniedException extends RuntimeException {

    public DepartmentConfigurationAccessDeniedException(final DepartmentId departmentId) {
        super("Current user cannot change configuration of department " + departmentId.value());
    }
}
