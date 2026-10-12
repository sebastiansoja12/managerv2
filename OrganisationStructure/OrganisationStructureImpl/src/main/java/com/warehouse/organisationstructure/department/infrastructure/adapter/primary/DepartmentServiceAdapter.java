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
import java.util.Optional;
import com.warehouse.organisationstructure.department.api.dto.DepartmentAddressDto;
import com.warehouse.organisationstructure.department.domain.enumeration.DepartmentType;
import com.warehouse.organisationstructure.department.domain.vo.ChangeDepartmentStatusCommand;
import com.warehouse.organisationstructure.department.infrastructure.adapter.primary.api.dto.AddressApi;
import com.warehouse.organisationstructure.department.infrastructure.adapter.primary.api.dto.ChangeDepartmentEmailApiRequest;
import com.warehouse.organisationstructure.department.infrastructure.adapter.primary.api.dto.ChangeDepartmentStatusApi;
import com.warehouse.organisationstructure.department.infrastructure.adapter.primary.api.dto.DepartmentCodeApi;
import com.warehouse.organisationstructure.department.infrastructure.adapter.primary.api.dto.UpdateAddressApiRequest;
import com.warehouse.organisationstructure.department.infrastructure.adapter.primary.validator.ChangeDepartmentEmailApiRequestValidator;
import com.warehouse.organisationstructure.department.infrastructure.adapter.primary.validator.ChangeDepartmentStatusApiValidator;
import com.warehouse.organisationstructure.department.infrastructure.adapter.primary.validator.UpdateAddressApiRequestValidator;
import com.warehouse.organisationstructure.department.domain.helper.Result;

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
        return department == null ? null : ResponseMapper.mapToDto(department);
    }

    @Override
    public Optional<DepartmentCode> findDepartmentCode(final DepartmentId departmentId) {
        return this.departmentPort.findDepartmentCode(departmentId);
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

    @Override
    public void changeAddress(final DepartmentCode departmentCode, final DepartmentAddressDto address) {
        final UpdateAddressApiRequest request = new UpdateAddressApiRequest(new DepartmentCodeApi(departmentCode.value()),
                address == null ? null : new AddressApi(address.city(), address.street(), address.postalCode(),
                        address.countryCode()));
        requireValid(new UpdateAddressApiRequestValidator().validateBody(request));
        this.departmentPort.changeAddress(RequestMapper.map(request));
    }

    @Override
    public void changeDepartmentType(final DepartmentCode departmentCode, final String departmentType) {
        this.departmentPort.changeDepartmentType(departmentCode, DepartmentType.valueOf(departmentType));
    }

    @Override
    public void changeEmail(final DepartmentCode departmentCode, final String email) {
        requireValid(new ChangeDepartmentEmailApiRequestValidator().validateBody(
                new ChangeDepartmentEmailApiRequest(new DepartmentCodeApi(departmentCode.value()), email)));
        this.departmentPort.changeEmail(departmentCode, email);
    }

    @Override
    public void changeStatus(final DepartmentCode departmentCode, final String status) {
        requireValid(new ChangeDepartmentStatusApiValidator().validateBody(
                new ChangeDepartmentStatusApi(new DepartmentCodeApi(departmentCode.value()), status)));
        this.departmentPort.changeStatus(new ChangeDepartmentStatusCommand(departmentCode, status));
    }

    @Override
    public void changeActive(final DepartmentCode departmentCode, final boolean active) {
        this.departmentPort.changeDepartmentActive(departmentCode, active);
    }

    private void requireValid(final Result<Void, List<String>> result) {
        if (result.isFailure()) {
            throw new IllegalArgumentException(String.join(", ", result.getFailure()));
        }
    }
}
