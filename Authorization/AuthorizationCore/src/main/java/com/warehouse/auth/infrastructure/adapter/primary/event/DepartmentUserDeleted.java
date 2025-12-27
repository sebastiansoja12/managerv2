package com.warehouse.auth.infrastructure.adapter.primary.event;

import com.warehouse.commonassets.identificator.DepartmentCode;

public class DepartmentUserDeleted {
    private final DepartmentCode departmentCode;

    public DepartmentUserDeleted(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }
}
