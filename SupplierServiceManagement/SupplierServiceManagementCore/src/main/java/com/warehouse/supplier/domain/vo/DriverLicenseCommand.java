package com.warehouse.supplier.domain.vo;

import com.warehouse.commonassets.identificator.SupplierCode;

public record DriverLicenseCommand(SupplierCode supplierCode, DriverLicense driverLicense) {
}
