package com.warehouse.terminal.domain.service;

import java.util.Objects;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.Department;
import com.warehouse.terminal.domain.port.secondary.DepartmentRepository;
import com.warehouse.terminal.domain.port.secondary.DeviceVersionRepository;

public class TerminalValidatorServiceImpl implements TerminalValidatorService {

    private final DeviceVersionRepository deviceVersionRepository;

    private final DepartmentRepository departmentRepository;

    public TerminalValidatorServiceImpl(final DeviceVersionRepository deviceVersionRepository,
                                        final DepartmentRepository departmentRepository) {
        this.deviceVersionRepository = deviceVersionRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void validateDepartment(final String departmentCode) {
        final Department department = this.departmentRepository.findByDepartmentCode(new DepartmentCode(departmentCode));
        if (Objects.isNull(department) || !department.isValid()) {
            throw new RuntimeException("User is not assigned to given department or department is not valid");
        }
    }

    @Override
    public void validateTerminalVersion(final DeviceId deviceId) {
        if (deviceVersionRepository.updateRequired(deviceId)) {
            throw new RuntimeException("Device needs to be updated");
        }
    }
}
