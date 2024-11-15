package com.warehouse.terminal.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;

public class Department {
    private DepartmentId departmentId;
    private DepartmentCode departmentCode;
    private Boolean valid;

    public Department(final DepartmentId departmentId, final DepartmentCode departmentCode, final Boolean valid) {
        this.departmentId = departmentId;
        this.departmentCode = departmentCode;
        this.valid = valid;
    }

    public DepartmentId getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final DepartmentId departmentId) {
        this.departmentId = departmentId;
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
