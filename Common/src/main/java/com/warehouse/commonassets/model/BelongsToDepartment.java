package com.warehouse.commonassets.model;

import com.warehouse.commonassets.identificator.DepartmentCode;

import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class BelongsToDepartment implements DepartmentContext {

    @Embedded
    private DepartmentCode departmentCode;

    @Override
    public DepartmentCode departmentCode() {
        return departmentCode;
    }

    @Override
    public void assignDepartment(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }
}
