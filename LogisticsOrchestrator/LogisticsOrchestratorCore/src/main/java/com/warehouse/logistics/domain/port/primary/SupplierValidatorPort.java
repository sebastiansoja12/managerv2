package com.warehouse.logistics.domain.port.primary;

import com.warehouse.terminal.information.Device;

public interface SupplierValidatorPort {
    void validateSupplierCode(final Device device);
}
