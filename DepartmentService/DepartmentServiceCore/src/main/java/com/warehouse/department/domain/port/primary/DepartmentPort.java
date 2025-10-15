package com.warehouse.department.domain.port.primary;

import java.util.List;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.model.DepartmentCreateRequest;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.DepartmentCreateResponse;
import com.warehouse.department.domain.vo.UpdateStreetRequest;

public interface DepartmentPort {

    Department findByDepartmentCode(DepartmentCode departmentCode);

    List<Department> findAll();

    void addDepartments(List<Department> departments);

    DepartmentCreateResponse createDepartments(final DepartmentCreateRequest request);

    void updateStreet(UpdateStreetRequest request);
}
