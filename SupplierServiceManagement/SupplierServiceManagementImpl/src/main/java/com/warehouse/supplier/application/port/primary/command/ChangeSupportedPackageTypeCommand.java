package com.warehouse.supplier.application.port.primary.command;

import com.warehouse.commonassets.enumeration.PackageType;
import com.warehouse.commonassets.identificator.SupplierCode;

import java.util.Set;

public record ChangeSupportedPackageTypeCommand(SupplierCode supplierCode, Set<PackageType> supportedPackageTypes) {
}
