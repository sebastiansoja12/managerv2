package com.warehouse.supplier.domain.vo;

import com.warehouse.commonassets.enumeration.PackageType;
import com.warehouse.commonassets.identificator.SupplierCode;

public record ChangeSupportedPackageTypeRequest(SupplierCode supplierCode, PackageType packageType) {
}
