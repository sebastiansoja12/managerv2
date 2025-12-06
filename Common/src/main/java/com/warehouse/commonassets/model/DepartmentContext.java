package com.warehouse.commonassets.model;

import com.warehouse.commonassets.identificator.DepartmentCode;

public interface DepartmentContext {
    DepartmentCode getDepartmentCode();
    void assignDepartment(final DepartmentCode departmentCode);
}
