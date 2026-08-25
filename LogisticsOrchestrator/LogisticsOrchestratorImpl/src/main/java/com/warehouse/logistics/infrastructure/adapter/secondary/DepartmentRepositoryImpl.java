package com.warehouse.logistics.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.department.api.dto.DepartmentDto;
import com.warehouse.logistics.domain.port.secondary.DepartmentRepository;

public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final DepartmentApiService departmentApiService;

    public DepartmentRepositoryImpl(final DepartmentApiService departmentApiService) {
        this.departmentApiService = departmentApiService;
    }

    @Override
    public boolean existsByCode(final DepartmentCode departmentCode) {
        return Boolean.TRUE.equals(departmentApiService.checkIfDepartmentExists(departmentCode));
    }

    @Override
    public DepartmentId findIdByCode(final DepartmentCode departmentCode) {
        final DepartmentDto department = departmentApiService.getDepartmentByCode(departmentCode);
        if (department == null || department.departmentId() == null) {
            throw new IllegalArgumentException("Department not found: " + departmentCode.getValue());
        }
        return new DepartmentId(department.departmentId());
    }
}
