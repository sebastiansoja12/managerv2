package com.warehouse.terminal.domain.service;

import java.util.Objects;
import java.util.Optional;

import com.warehouse.commonassets.exception.ProcessFailureDetails;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.exception.DeviceValidationException;
import com.warehouse.terminal.domain.model.Department;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.port.secondary.DepartmentServicePort;
import com.warehouse.terminal.domain.port.secondary.DeviceGenericRepository;
import com.warehouse.terminal.domain.port.secondary.DeviceVersionRepository;
import com.warehouse.terminal.domain.port.secondary.UserServicePort;
import com.warehouse.terminal.domain.vo.DeviceValidationRequest;
import com.warehouse.terminal.domain.vo.User;

public class DeviceValidatorServiceImpl implements DeviceValidatorService {

    private final DeviceVersionRepository deviceVersionRepository;

    private final DeviceGenericRepository deviceRepository;

    private final DepartmentServicePort departmentServicePort;

    private final UserServicePort userServicePort;

    public DeviceValidatorServiceImpl(final DeviceVersionRepository deviceVersionRepository,
                                      final DeviceGenericRepository deviceRepository,
                                      final DepartmentServicePort departmentServicePort,
                                      final UserServicePort userServicePort) {
        this.deviceVersionRepository = deviceVersionRepository;
        this.deviceRepository = deviceRepository;
        this.departmentServicePort = departmentServicePort;
        this.userServicePort = userServicePort;
    }

    @Override
    public void validateDepartment(final String departmentCode) {
        final Department department = this.departmentServicePort.getDepartment(new DepartmentCode(departmentCode));
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
        final User user = userServicePort.findUserByUsername(request.username().value());
        final Optional<Device> device = deviceRepository.findById(request.deviceId())
                .stream()
                .filter(dev -> user != null && dev.getUserId().equals(user.userId()))
                .findAny();
        final boolean deviceExists = device.isPresent();
        final boolean departmentExists = departmentServicePort.getDepartment(request.departmentCode()) != null;
        final boolean deviceValid = deviceExists && device.get().isActive();

		if (user == null || !deviceExists || !departmentExists || !deviceValid
				|| Boolean.FALSE.equals(request.active())) {
			throw new DeviceValidationException(ProcessFailureDetails.now(request.processId(),
					request.sourceServiceType().name(), request.targetServiceType().name(),
					"Device is not valid:" + request.deviceId()));
		}
    }
}
