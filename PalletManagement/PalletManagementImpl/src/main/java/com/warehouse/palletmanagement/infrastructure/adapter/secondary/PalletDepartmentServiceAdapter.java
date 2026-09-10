package com.warehouse.palletmanagement.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.organisationstructure.department.api.dto.DepartmentDto;
import com.warehouse.palletmanagement.application.port.secondary.DepartmentServicePort;
import com.warehouse.palletmanagement.domain.vo.PalletDepartment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PalletDepartmentServiceAdapter implements DepartmentServicePort {

    private static final Logger log = LoggerFactory.getLogger(PalletDepartmentServiceAdapter.class);

    private final DepartmentApiService departmentApiService;

    public PalletDepartmentServiceAdapter(final DepartmentApiService departmentApiService) {
        this.departmentApiService = departmentApiService;
    }

    @Override
    public Optional<PalletDepartment> findDepartment(final DepartmentId departmentId) {
        try {
            return Optional.ofNullable(this.departmentApiService.getDepartmentById(departmentId))
                    .map(department -> map(departmentId, department));
        } catch (final RuntimeException exception) {
            log.warn("Department {} could not be read: {}", departmentId.getValue(), exception.getMessage());
            return Optional.empty();
        }
    }

    private PalletDepartment map(final DepartmentId departmentId, final DepartmentDto department) {
        return new PalletDepartment(departmentId, department.departmentCode(), department.city(), department.street(),
                department.zipCode(), department.countryCode());
    }
}
