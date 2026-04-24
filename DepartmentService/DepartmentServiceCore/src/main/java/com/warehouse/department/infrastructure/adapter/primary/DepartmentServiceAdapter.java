package com.warehouse.department.infrastructure.adapter.primary;

import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.department.api.dto.CoordinatesDto;
import com.warehouse.department.api.dto.DepartmentCode;
import com.warehouse.department.api.dto.DepartmentDto;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.port.primary.DepartmentPort;
import com.warehouse.department.infrastructure.adapter.primary.mapper.ResponseMapper;

import java.util.List;

public class DepartmentServiceAdapter implements DepartmentApiService {

    private final DepartmentPort departmentPort;

    public DepartmentServiceAdapter(final DepartmentPort departmentPort) {
        this.departmentPort = departmentPort;
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return this.departmentPort.findAll()
                .stream()
                .filter(dep ->
                        dep.getDepartmentType().equals(DepartmentType.BRANCH)
                                || dep.getDepartmentType().equals(DepartmentType.WAREHOUSE))
                .map(dep -> new DepartmentDto(dep.getDepartmentCode().getValue(),
                        dep.getCity(), dep.getStreet(), dep.getCountryCode().name(),
                        dep.getPostalCode(), new CoordinatesDto(dep.getCoordinates().lat(), dep.getCoordinates().lon())))
                .toList();
    }

    @Override
    public DepartmentDto getDepartmentByCode(final DepartmentCode departmentCode) {
        final Department department = this.departmentPort.findByDepartmentCode(
                new com.warehouse.department.domain.vo.DepartmentCode(departmentCode.value()));
        return ResponseMapper.mapToDto(department);
    }
}
