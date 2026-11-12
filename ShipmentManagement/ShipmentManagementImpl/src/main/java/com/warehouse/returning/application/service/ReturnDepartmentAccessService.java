package com.warehouse.returning.application.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.returning.application.port.secondary.CurrentUserServicePort;
import com.warehouse.returning.domain.exception.ReturnDepartmentAccessDeniedException;

public class ReturnDepartmentAccessService {

    private final CurrentUserServicePort currentUserServicePort;

    public ReturnDepartmentAccessService(final CurrentUserServicePort currentUserServicePort) {
        this.currentUserServicePort = currentUserServicePort;
    }

    public void requireAccess(final DepartmentId departmentId) {
        if (!currentUserServicePort.canAccessDepartment(departmentId)) {
            throw new ReturnDepartmentAccessDeniedException("Return belongs to another department");
        }
    }

    public void requireAccess(final DepartmentId assignedDepartmentId, final DepartmentId returnedDepartmentId) {
        if (!currentUserServicePort.canAccessDepartment(assignedDepartmentId)
                && !currentUserServicePort.canAccessDepartment(returnedDepartmentId)) {
            throw new ReturnDepartmentAccessDeniedException("Return belongs to another department");
        }
    }
}
