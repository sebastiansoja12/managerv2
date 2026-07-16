package com.warehouse.supplier.domain.vo;

import com.warehouse.commonassets.enumeration.PackageType;
import com.warehouse.commonassets.identificator.SupplierCode;

import java.util.Set;

public record ChangeSupportedPackageTypeCommand(SupplierCode supplierCode, Set<PackageType> supportedPackageTypes) {
}
