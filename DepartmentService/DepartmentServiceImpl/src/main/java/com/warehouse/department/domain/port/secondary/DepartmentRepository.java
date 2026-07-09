package com.warehouse.department.domain.port.secondary;

import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.department.domain.model.Department;

public interface DepartmentRepository {

    void createOrUpdate(final Department department);

    Department findByDepartmentCode(final DepartmentCode departmentCode);

    List<Department> findAll();

    Boolean checkExists(final DepartmentCode departmentCode);
}
