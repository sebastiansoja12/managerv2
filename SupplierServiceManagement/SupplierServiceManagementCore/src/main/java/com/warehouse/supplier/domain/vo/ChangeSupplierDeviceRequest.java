package com.warehouse.supplier.domain.vo;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.SupplierCode;

public record ChangeSupplierDeviceRequest(SupplierCode supplierCode, DeviceId deviceId) {
}
