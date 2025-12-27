package com.warehouse.department.api;

import java.util.List;

import com.warehouse.department.api.dto.DepartmentDto;

public interface DepartmentApiService {
    List<DepartmentDto> getAllDepartments();
}
