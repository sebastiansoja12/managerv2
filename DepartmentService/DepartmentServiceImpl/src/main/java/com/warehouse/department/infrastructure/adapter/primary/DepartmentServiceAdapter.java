package com.warehouse.department.infrastructure.adapter.primary;

import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.department.api.dto.DepartmentDto;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.port.primary.DepartmentPort;
import com.warehouse.department.infrastructure.adapter.primary.mapper.ResponseMapper;

public class DepartmentServiceAdapter implements DepartmentApiService {

    private final DepartmentPort departmentPort;

    public DepartmentServiceAdapter(final DepartmentPort departmentPort) {
        this.departmentPort = departmentPort;
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return this.departmentPort.findAll()
                .stream()
                .map(ResponseMapper::mapToDto)
                .toList();
    }

    @Override
    public DepartmentDto getDepartmentByCode(final DepartmentCode departmentCode) {
        final Department department = this.departmentPort.findByDepartmentCode(departmentCode);
        return ResponseMapper.mapToDto(department);
    }

    @Override
    public Boolean checkIfDepartmentExists(final DepartmentCode departmentCode) {
        return this.departmentPort.checkExists(departmentCode);
    }
}
