package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.vo.DeviceValidationRequest;

public interface DeviceValidatorService {
    void validateDepartment(final String departmentCode);
    void validateTerminalVersion(final DeviceId deviceId);
    void validateDevice(final DeviceValidationRequest request);
}
