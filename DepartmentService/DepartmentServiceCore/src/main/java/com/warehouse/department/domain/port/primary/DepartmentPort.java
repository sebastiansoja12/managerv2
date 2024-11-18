package com.warehouse.department.domain.port.primary;

import java.util.List;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.UpdateStreetRequest;

public interface DepartmentPort {

    Department findByDepartmentCode(DepartmentCode departmentCode);

    List<Department> findAll();

    void addDepartments(List<Department> departments);

    void updateStreet(UpdateStreetRequest request);
}
