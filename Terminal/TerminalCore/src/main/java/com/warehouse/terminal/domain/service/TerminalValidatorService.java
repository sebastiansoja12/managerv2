package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.Terminal;

public interface TerminalValidatorService {
    void validateDepartment(final String departmentCode);
    void validateTerminalVersion(final DeviceId deviceId);
    void validateDevice(final Terminal terminal);
}
