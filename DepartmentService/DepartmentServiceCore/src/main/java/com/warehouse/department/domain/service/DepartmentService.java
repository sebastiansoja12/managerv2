package com.warehouse.department.domain.service;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.vo.DepartmentCode;

public interface DepartmentService {
    void createDepartment(final Department department);
    Department findByDepartmentCode(final DepartmentCode departmentCode);
}
