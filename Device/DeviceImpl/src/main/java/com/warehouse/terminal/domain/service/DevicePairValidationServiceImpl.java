package com.warehouse.terminal.domain.service;

import java.util.Objects;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.exception.DeviceAccessException;
import com.warehouse.terminal.domain.model.Device;

public class DevicePairValidationServiceImpl implements DevicePairValidationService {

    private final UserService userService;

    public DevicePairValidationServiceImpl(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public void validateOperator(final Device device, final UserId userId) {
        if (!DeviceStatus.ACTIVE.equals(device.getStatus())) {
            throw DeviceAccessException.deviceNotActive();
        }
        if (device.getOwnership() != null && device.getOwnership().isShared()) {
            if (!Boolean.TRUE.equals(this.userService.existsByUserIdAndDepartmentCode(userId,
                    device.getDepartmentCode()))) {
                throw DeviceAccessException.userOutsideDeviceDepartment();
            }
            return;
        }
        if (!Objects.equals(device.getUserId(), userId)) {
            throw DeviceAccessException.deviceNotAssignedToUser();
        }
    }
}
