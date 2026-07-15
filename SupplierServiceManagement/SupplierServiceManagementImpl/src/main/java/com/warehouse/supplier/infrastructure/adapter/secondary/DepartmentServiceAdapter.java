package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.supplier.domain.port.secondary.DepartmentServicePort;

public class DepartmentServiceAdapter implements DepartmentServicePort {

    private final DepartmentApiService departmentApiService;

    public DepartmentServiceAdapter(final DepartmentApiService departmentApiService) {
        this.departmentApiService = departmentApiService;
    }

    @Override
    public Result<Void, Void> validateDepartmentCode(final DepartmentCode departmentCode) {
        final Boolean existsDepartment = departmentApiService.checkIfDepartmentExists(departmentCode);
        if (existsDepartment) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @Override
    public DepartmentId getDepartmentId(final DepartmentCode departmentCode) {
        return new DepartmentId(departmentApiService.getDepartmentByCode(departmentCode).departmentId());
    }

    @Override
    public DepartmentCode getDepartmentCode(final DepartmentId departmentId) {
        return new DepartmentCode(departmentApiService.getDepartmentById(departmentId).departmentCode());
    }
}
