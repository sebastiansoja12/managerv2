package com.warehouse.supplier.infrastructure.adapter.primary.dto;

import com.warehouse.commonassets.identificator.SupplierCode;

public record DriverLicenseApiRequest(SupplierCode supplierCode, DriverLicenseApi driverLicense) {
}
