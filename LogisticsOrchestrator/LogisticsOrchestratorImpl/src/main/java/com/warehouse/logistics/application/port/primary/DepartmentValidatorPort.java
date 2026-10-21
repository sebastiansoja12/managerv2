package com.warehouse.logistics.application.port.primary;

import com.warehouse.terminal.DeviceInformation;

public interface DepartmentValidatorPort {
    void validateDepartment(final DeviceInformation deviceInformation);
}
