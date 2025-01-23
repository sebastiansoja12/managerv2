package com.warehouse.delivery.domain.port.primary;

import com.warehouse.terminal.information.Device;

public interface SupplierValidatorPort {
    void validateSupplierCode(final Device device);
}
