package com.warehouse.commonassets.model;

import com.warehouse.commonassets.identificator.DepartmentCode;

import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.envers.Audited;

@MappedSuperclass
@Audited
public abstract class BelongsToDepartment implements DepartmentContext {

    @Embedded
    private DepartmentCode departmentCode;

    @Override
    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    @Override
    public void assignDepartment(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }
}
