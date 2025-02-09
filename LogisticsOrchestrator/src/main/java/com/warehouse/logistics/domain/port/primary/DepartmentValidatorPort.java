package com.warehouse.logistics.domain.port.primary;

import com.warehouse.terminal.information.Device;

public interface DepartmentValidatorPort {
    void validateDepartment(final Device device);
}
