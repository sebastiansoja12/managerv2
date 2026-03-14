package com.warehouse.terminal.domain.service;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.command.DevicePairRequest;
import com.warehouse.terminal.domain.port.secondary.DepartmentRepository;

public class DevicePairValidationServiceImpl implements DevicePairValidationService {

    private final DeviceGenericService deviceGenericService;

    private final UserService userService;

    private final DepartmentRepository departmentRepository;

    public DevicePairValidationServiceImpl(final DeviceGenericService deviceGenericService,
                                           final UserService userService,
                                           final DepartmentRepository departmentRepository) {
        this.deviceGenericService = deviceGenericService;
        this.userService = userService;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Device validate(final DevicePairRequest request) {
        validateRequiredFields(request);

        final Device device = this.deviceGenericService.findByExternalSystemId(request.getExternalSystemId())
                .orElseThrow(() -> new RestException(404, "Device not found"));

        validateDepartment(request.getDepartmentCode());
        validateUserDepartment(request.getUserId(), request.getDepartmentCode());
        validateDeviceDepartment(device, request.getDepartmentCode());
        validateDeviceUser(device, request.getUserId());
        validateDeviceActive(device);

        return device;
    }

    private void validateRequiredFields(final DevicePairRequest request) {
        if (request == null) {
            throw new RestException(400, "Device pair request is required");
        }
        if (StringUtils.isBlank(request.getExternalSystemId())) {
            throw new RestException(400, "External system id is required");
        }
        if (request.getDepartmentCode() == null || StringUtils.isBlank(request.getDepartmentCode().value())) {
            throw new RestException(400, "Department code is required");
        }
        if (request.getUserId() == null || request.getUserId().value() == null) {
            throw new RestException(400, "User id is required");
        }
    }

    private void validateDepartment(final DepartmentCode departmentCode) {
        if (!this.departmentRepository.existsByDepartmentCode(departmentCode)) {
            throw new RestException(404, "Department not found");
        }
    }

    private void validateUserDepartment(final UserId userId, final DepartmentCode departmentCode) {
        if (!this.userService.existsByUserIdAndDepartmentCode(userId, departmentCode)) {
            throw new RestException(403, "User does not belong to department");
        }
    }

    private void validateDeviceDepartment(final Device device, final DepartmentCode departmentCode) {
        final DepartmentCode deviceDepartmentCode = device.getDepartmentCode();
        if (deviceDepartmentCode == null || !Objects.equals(deviceDepartmentCode.value(), departmentCode.value())) {
            throw new RestException(403, "Device does not belong to department");
        }
    }

    private void validateDeviceUser(final Device device, final UserId userId) {
        if (!Objects.equals(device.getUserId(), userId)) {
            throw new RestException(403, "Device does not belong to user");
        }
    }

    private void validateDeviceActive(final Device device) {
        if (!DeviceStatus.ACTIVE.equals(device.getStatus())) {
            throw new RestException(403, "Device is not active");
        }
    }
}
