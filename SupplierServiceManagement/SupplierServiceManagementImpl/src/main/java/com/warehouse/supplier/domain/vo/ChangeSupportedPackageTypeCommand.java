package com.warehouse.supplier.domain.vo;

import com.warehouse.commonassets.enumeration.PackageType;
import com.warehouse.commonassets.identificator.SupplierCode;

public record ChangeSupportedPackageTypeCommand(SupplierCode supplierCode, PackageType packageType) {
}
