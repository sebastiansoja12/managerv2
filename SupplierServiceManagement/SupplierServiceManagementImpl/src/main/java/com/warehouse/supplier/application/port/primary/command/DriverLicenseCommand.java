package com.warehouse.supplier.application.port.primary.command;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.supplier.domain.vo.DriverLicense;

public record DriverLicenseCommand(SupplierCode supplierCode, DriverLicense driverLicense) {
}
