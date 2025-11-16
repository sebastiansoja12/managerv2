package com.warehouse.department.infrastructure.adapter.primary.event;

import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentCodeApi;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.UserIdApi;

public class DepartmentAdminEvent {
    private final DepartmentCodeApi departmentCode;
    private final UserIdApi userId;

    public DepartmentAdminEvent(final DepartmentCodeApi departmentCode, final UserIdApi userId) {
        this.departmentCode = departmentCode;
        this.userId = userId;
    }

    public DepartmentCodeApi getDepartmentCode() {
        return departmentCode;
    }

    public UserIdApi getUserId() {
        return userId;
    }
}
