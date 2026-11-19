package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.supplier.application.port.secondary.DepartmentServicePort;
import com.warehouse.supplier.infrastructure.adapter.secondary.mapper.OutputRequestMapper;

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
        return OutputRequestMapper.mapDepartmentId(departmentApiService.getDepartmentByCode(departmentCode));
    }

    @Override
    public DepartmentCode getDepartmentCode(final DepartmentId departmentId) {
        return OutputRequestMapper.mapDepartmentCode(departmentApiService.getDepartmentById(departmentId));
    }
}
