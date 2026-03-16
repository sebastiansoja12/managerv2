package com.warehouse.logistics.domain.port.primary;

import com.warehouse.terminal.DeviceInformation;

public interface DepartmentValidatorPort {
    void validateDepartment(final DeviceInformation deviceInformation);
}
