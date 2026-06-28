package com.warehouse.department.domain.event;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

public class DepartmentAdminChanged {
    private final DepartmentCode departmentCode;
    private final UserId userId;

    public DepartmentAdminChanged(final DepartmentCode departmentCode, final UserId userId) {
        this.departmentCode = departmentCode;
        this.userId = userId;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public UserId getUserId() {
        return userId;
    }
}
