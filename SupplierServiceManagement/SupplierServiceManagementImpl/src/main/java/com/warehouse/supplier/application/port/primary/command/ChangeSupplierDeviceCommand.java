package com.warehouse.supplier.application.port.primary.command;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.SupplierCode;

public record ChangeSupplierDeviceCommand(SupplierCode supplierCode, DeviceId deviceId) {
}
