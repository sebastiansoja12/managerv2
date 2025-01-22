package com.warehouse.terminal.domain.service;

import java.util.Objects;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.Department;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.port.secondary.*;

public class TerminalValidatorServiceImpl implements TerminalValidatorService {

    private final DeviceVersionRepository deviceVersionRepository;

    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;

    private final SupplierRepository supplierRepository;

    private final DeviceRepository deviceRepository;

    public TerminalValidatorServiceImpl(final DeviceVersionRepository deviceVersionRepository,
                                        final DepartmentRepository departmentRepository,
                                        final UserRepository userRepository,
                                        final SupplierRepository supplierRepository,
                                        final DeviceRepository deviceRepository) {
        this.deviceVersionRepository = deviceVersionRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.supplierRepository = supplierRepository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public void validateDepartment(final String departmentCode) {
        final Department department = this.departmentRepository.findByDepartmentCode(new DepartmentCode(departmentCode));
        if (Objects.isNull(department) || !department.isActive()) {
            throw new RuntimeException("User is not assigned to given department or department is not valid");
        }
    }

    @Override
    public void validateTerminalVersion(final DeviceId deviceId) {
        if (deviceVersionRepository.updateRequired(deviceId)) {
            throw new RuntimeException("Device needs to be updated");
        }
    }

    @Override
    public void validateDevice(final Terminal terminal) {
        final Terminal device = (Terminal) this.deviceRepository.findById(terminal.getDeviceId());
        // TODO bug
        //final boolean userExists = userRepository.findById(terminal.getUserId()) != null;
        final boolean userExists = true;
        final boolean deviceExists = deviceRepository.findById(terminal.getDeviceId()) != null;
        final boolean departmentExists = departmentRepository.existsByDepartmentCode(new DepartmentCode(terminal.getDepartmentCode()));
        final boolean deviceValid = device != null && device.isActive();

        if (!userExists || !deviceExists || !departmentExists || !deviceValid) {
            throw new RuntimeException("Device is not valid");
        }
    }
}
