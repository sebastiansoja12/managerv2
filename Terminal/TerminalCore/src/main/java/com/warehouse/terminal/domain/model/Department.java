package com.warehouse.terminal.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;

public class Department {
    private DepartmentCode departmentCode;
    private Boolean valid;

    public Department(final DepartmentCode departmentCode, final Boolean valid) {
        this.departmentCode = departmentCode;
        this.valid = valid;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(final Boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }
}
