package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.identificator.DeviceId;

public interface TerminalValidatorService {
    void validateDepartment(final String departmentCode);
    void validateTerminalVersion(final DeviceId deviceId);
}
