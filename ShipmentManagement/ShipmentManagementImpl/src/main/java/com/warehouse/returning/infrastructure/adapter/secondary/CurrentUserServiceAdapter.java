package com.warehouse.returning.infrastructure.adapter.secondary;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.returning.application.port.secondary.CurrentUserServicePort;
import org.springframework.stereotype.Component;

@Component("returningCurrentUserServiceAdapter")
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
