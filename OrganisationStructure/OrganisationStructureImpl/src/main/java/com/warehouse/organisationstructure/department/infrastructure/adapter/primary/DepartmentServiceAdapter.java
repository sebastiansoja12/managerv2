package com.warehouse.organisationstructure.department.infrastructure.adapter.primary;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.organisationstructure.department.api.dto.CreateDepartmentApiRequest;
import com.warehouse.organisationstructure.department.api.dto.DepartmentCodeDto;
import com.warehouse.organisationstructure.department.api.dto.DepartmentDetailsDto;
import com.warehouse.organisationstructure.department.api.dto.DepartmentDirectoryEntryDto;
import com.warehouse.organisationstructure.department.api.dto.DepartmentDto;
import com.warehouse.organisationstructure.department.domain.model.Department;
import com.warehouse.organisationstructure.department.application.port.primary.DepartmentPort;
import com.warehouse.organisationstructure.department.infrastructure.adapter.primary.mapper.RequestMapper;
import com.warehouse.organisationstructure.department.infrastructure.adapter.primary.mapper.ResponseMapper;

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
                .map(ResponseMapper::mapToDto)
                .toList();
    }

    @Override
    public List<DepartmentDirectoryEntryDto> getDepartmentDirectory() {
        return this.departmentPort.findAll()
                .stream()
                .map(ResponseMapper::mapToDirectoryEntryDto)
                .toList();
    }

    @Override
    public DepartmentDto getDepartmentByCode(final DepartmentCode departmentCode) {
        final Department department = this.departmentPort.findByDepartmentCode(departmentCode);
        return ResponseMapper.mapToDto(department);
    }

    @Override
    public DepartmentDto getDepartmentById(final DepartmentId departmentId) {
        final Department department = this.departmentPort.findByDepartmentId(departmentId);
        return ResponseMapper.mapToDto(department);
    }

    @Override
    public Boolean checkIfDepartmentExists(final DepartmentCode departmentCode) {
        return this.departmentPort.checkExists(departmentCode);
    }

    @Override
    public Boolean checkIfDepartmentExists(final DepartmentId departmentId) {
        return this.departmentPort.findByDepartmentId(departmentId) != null;
    }

    @Override
    public boolean checkIfAnyExistsWithCountryCode(final CountryCode countryCode) {
        return this.departmentPort.checkExistsAnyWithCountryCode(countryCode);
    }

    @Override
    public List<DepartmentDetailsDto> getDepartmentDetails() {
        return this.departmentPort.findAll()
                .stream()
                .map(ResponseMapper::mapToDetailsDto)
                .toList();
    }

    @Override
    public DepartmentCodeDto createDepartment(final CreateDepartmentApiRequest request) {
        this.departmentPort.createDepartments(RequestMapper.map(request));
        return request.departmentCode();
    }
}
