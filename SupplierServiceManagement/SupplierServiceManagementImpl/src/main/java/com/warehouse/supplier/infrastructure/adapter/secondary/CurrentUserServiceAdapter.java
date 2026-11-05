package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.supplier.domain.port.secondary.CurrentUserServicePort;

public class CurrentUserServiceAdapter implements CurrentUserServicePort {

    private final CurrentUserApiService currentUserApiService;

    public CurrentUserServiceAdapter(final CurrentUserApiService currentUserApiService) {
        this.currentUserApiService = currentUserApiService;
    }

    @Override
    public boolean canAccessDepartment(final DepartmentId departmentId) {
        return departmentId != null && this.currentUserApiService.canAccessDepartment(departmentId);
    }
}
