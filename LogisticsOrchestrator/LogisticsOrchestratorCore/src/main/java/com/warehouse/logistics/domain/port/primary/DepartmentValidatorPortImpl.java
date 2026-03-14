package com.warehouse.logistics.domain.port.primary;

import com.warehouse.logistics.domain.port.secondary.DepartmentRepository;
import com.warehouse.logistics.domain.vo.Department;
import com.warehouse.logistics.infrastructure.adapter.primary.exception.RestException;
import com.warehouse.terminal.DeviceInformation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DepartmentValidatorPortImpl implements DepartmentValidatorPort {

    private final DepartmentRepository departmentRepository;

    public DepartmentValidatorPortImpl(final DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void validateDepartment(final DeviceInformation deviceInformation) {
        log.info("Validating department {} from device {}", deviceInformation.getDepartmentCode(), deviceInformation);
        final Department department = this.departmentRepository.findByCode(deviceInformation.getDepartmentCode());
        if (department == null || !department.isActive()) {
            log.error("User {} validation for department {} failed. Used Device: [{}]", deviceInformation.getUsername(),
                    deviceInformation.getDepartmentCode(), deviceInformation);
            throw new RestException(400, "Department is not valid");
        }
        log.info("Device [{}] department validated", deviceInformation);
    }
}
