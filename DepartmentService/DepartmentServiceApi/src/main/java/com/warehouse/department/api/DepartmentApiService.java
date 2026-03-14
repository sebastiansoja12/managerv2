package com.warehouse.department.api;

import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.department.api.dto.DepartmentDto;

public interface DepartmentApiService {
    List<DepartmentDto> getAllDepartments();
    DepartmentDto getDepartmentByCode(final DepartmentCode departmentCode);
    Boolean checkIfDepartmentExists(final DepartmentCode departmentCode);
}
