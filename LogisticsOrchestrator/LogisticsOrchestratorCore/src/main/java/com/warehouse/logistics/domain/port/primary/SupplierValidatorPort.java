package com.warehouse.logistics.domain.port.primary;

import com.warehouse.terminal.DeviceInformation;

public interface SupplierValidatorPort {
    void validateSupplierCode(final DeviceInformation deviceInformation);
}
