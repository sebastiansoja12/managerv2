package com.warehouse.supplier.domain.vo;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.SupplierCode;

public record ChangeSupplierDeviceCommand(SupplierCode supplierCode, DeviceId deviceId) {
}
