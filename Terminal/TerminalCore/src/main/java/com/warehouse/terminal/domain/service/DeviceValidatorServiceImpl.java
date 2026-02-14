package com.warehouse.terminal.domain.service;

import java.util.Objects;
import java.util.Optional;

import com.warehouse.commonassets.exception.ProcessFailureDetails;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.exception.DeviceValidationException;
import com.warehouse.terminal.domain.model.Department;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.port.secondary.*;
import com.warehouse.terminal.domain.vo.DeviceValidationRequest;

public class DeviceValidatorServiceImpl implements DeviceValidatorService {

    private final DeviceVersionRepository deviceVersionRepository;

    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;

    private final SupplierRepository supplierRepository;

    private final DeviceGenericRepository deviceRepository;

    private final DepartmentServicePort departmentServicePort;

    public DeviceValidatorServiceImpl(final DeviceVersionRepository deviceVersionRepository,
                                      final DepartmentRepository departmentRepository,
                                      final UserRepository userRepository,
                                      final SupplierRepository supplierRepository,
                                      final DeviceGenericRepository deviceRepository,
                                      final DepartmentServicePort departmentServicePort) {
        this.deviceVersionRepository = deviceVersionRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.supplierRepository = supplierRepository;
        this.deviceRepository = deviceRepository;
        this.departmentServicePort = departmentServicePort;
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
    public void validateDevice(final DeviceValidationRequest request) {
        final boolean userExists = userRepository.existsByUsername(request.username());
        final Optional<Device> device = deviceRepository.findById(request.deviceId());
        final boolean deviceExists = device.isPresent();
        final boolean departmentExists = departmentServicePort.getDepartment(request.departmentCode()) != null;
        final boolean deviceValid = deviceExists && device.get().isActive();

		if (!userExists || !deviceExists || !departmentExists || !deviceValid
				|| Boolean.FALSE.equals(request.active())) {
			throw new DeviceValidationException(ProcessFailureDetails.now(request.processId(),
					request.sourceServiceType().name(), request.targetServiceType().name(),
					"Device is not valid:" + request.deviceId()));
		}
    }
}
