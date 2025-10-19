package com.warehouse.department.domain.port.secondary;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.vo.DepartmentCode;

import java.util.List;

public interface DepartmentRepository {

    void createOrUpdate(final Department department);

    Department findByCode(final DepartmentCode departmentCode);

    List<Department> findAll();

}
