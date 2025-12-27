package com.warehouse.logistics.domain.port.primary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.logistics.domain.port.secondary.DepartmentRepository;
import com.warehouse.logistics.domain.vo.Department;
import com.warehouse.logistics.infrastructure.adapter.primary.exception.RestException;
import com.warehouse.terminal.information.Device;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DepartmentValidatorPortImpl implements DepartmentValidatorPort {

    private final DepartmentRepository departmentRepository;

    public DepartmentValidatorPortImpl(final DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void validateDepartment(final Device device) {
        log.info("Validating department {} from device {}", device.getDepartmentCode(), device);
        final Department department = this.departmentRepository.findByCode(new DepartmentCode(device.getDepartmentCode()));
        if (department == null || !department.isActive()) {
            log.error("User {} validation for department {} failed. Used Device: [{}]", device.getUsername(),
                    device.getDepartmentCode(), device);
            throw new RestException(400, "Department is not valid");
        }
        log.info("Device [{}] department validated", device);
    }
}
