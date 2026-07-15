package com.warehouse.pickuppoint.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.department.api.dto.DepartmentDirectoryEntryDto;
import com.warehouse.department.api.dto.DepartmentStatusDto;
import com.warehouse.pickuppoint.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.pickuppoint.domain.vo.PickupPointDepartment;

import java.util.List;
import java.util.Optional;

public class DepartmentDirectoryServiceAdapter implements DepartmentDirectoryServicePort {

    private final DepartmentApiService departmentApiService;

    public DepartmentDirectoryServiceAdapter(final DepartmentApiService departmentApiService) {
        this.departmentApiService = departmentApiService;
    }

    @Override
    public Optional<PickupPointDepartment> findById(final DepartmentId departmentId) {
        return getCurrentOperatorDepartments().stream()
                .filter(department -> department.departmentId().equals(departmentId))
                .findFirst();
    }

    @Override
    public List<PickupPointDepartment> getCurrentOperatorDepartments() {
        return this.departmentApiService.getDepartmentDirectory().stream()
                .map(this::toDomain)
                .toList();
    }

    private PickupPointDepartment toDomain(final DepartmentDirectoryEntryDto department) {
        return new PickupPointDepartment(
                new DepartmentId(department.departmentId().value()),
                new DepartmentCode(department.departmentCode().value()),
                department.status() == DepartmentStatusDto.ACTIVE);
    }
}
