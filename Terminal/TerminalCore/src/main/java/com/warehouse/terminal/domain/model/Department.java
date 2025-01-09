package com.warehouse.terminal.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;

public class Department {
    private DepartmentCode departmentCode;
    private Boolean active;

    public Department(final DepartmentCode departmentCode, final Boolean active) {
        this.departmentCode = departmentCode;
        this.active = active;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
