package com.warehouse.mcp.infrastructure.adapter.secondary;

import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.mcp.application.port.secondary.DepartmentServicePort;
import com.warehouse.mcp.domain.vo.DepartmentDetails;
import com.warehouse.mcp.domain.vo.DepartmentEntry;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.organisationstructure.department.api.dto.DepartmentDirectoryEntryDto;
import com.warehouse.organisationstructure.department.api.dto.DepartmentDto;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.mcp.domain.vo.DepartmentAddress;
import com.warehouse.organisationstructure.department.api.dto.DepartmentAddressDto;

public class DepartmentServiceAdapter implements DepartmentServicePort {

    private final DepartmentApiService departmentApiService;

    public DepartmentServiceAdapter(final DepartmentApiService departmentApiService) {
        this.departmentApiService = departmentApiService;
    }

    @Override
    public List<DepartmentEntry> findAllDepartments() {
        return this.departmentApiService.getDepartmentDirectory().stream().map(this::map).toList();
    }

    @Override
    public DepartmentDetails findDepartment(final DepartmentCode departmentCode) {
        final DepartmentDto department = this.departmentApiService.getDepartmentByCode(departmentCode);
        return new DepartmentDetails(new DepartmentCode(department.departmentCode()), department.city(),
                department.street(), department.zipCode(), department.countryCode(),
                department.coordinates() != null ? department.coordinates().latitude() : null,
                department.coordinates() != null ? department.coordinates().longitude() : null);
    }

    @Override
    public void changeAddress(final DepartmentCode departmentCode, final DepartmentAddress address) {
        this.departmentApiService.changeAddress(departmentCode, new DepartmentAddressDto(address.city(),
                address.street(), address.postalCode(), address.countryCode()));
    }

    @Override
    public void changeType(final DepartmentCode departmentCode, final String type) {
        this.departmentApiService.changeDepartmentType(departmentCode, type);
    }

    @Override
    public void changeEmail(final DepartmentCode departmentCode, final String email) {
        this.departmentApiService.changeEmail(departmentCode, email);
    }

    @Override
    public void changeStatus(final DepartmentCode departmentCode, final String status) {
        this.departmentApiService.changeStatus(departmentCode, status);
    }

    @Override
    public void changeActive(final DepartmentCode departmentCode, final boolean active) {
        this.departmentApiService.changeActive(departmentCode, active);
    }

    private DepartmentEntry map(final DepartmentDirectoryEntryDto department) {
        return new DepartmentEntry(
                department.departmentId() != null ? new DepartmentId(department.departmentId().value()) : null,
                new DepartmentCode(department.departmentCode().value()),
                department.departmentType() != null ? department.departmentType().name() : null,
                department.status() != null ? department.status().name() : null);
    }
}
