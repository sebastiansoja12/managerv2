package com.warehouse.department.api;

import java.util.List;

import com.warehouse.department.api.dto.DepartmentCode;
import com.warehouse.department.api.dto.DepartmentDto;

public interface DepartmentApiService {
    List<DepartmentDto> getAllDepartments();
    DepartmentDto getDepartmentByCode(final DepartmentCode departmentCode);
}
