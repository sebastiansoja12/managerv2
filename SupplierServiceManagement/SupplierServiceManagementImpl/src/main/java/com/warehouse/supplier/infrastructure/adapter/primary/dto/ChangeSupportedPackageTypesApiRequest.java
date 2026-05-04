package com.warehouse.supplier.infrastructure.adapter.primary.dto;

import java.util.Set;

public record ChangeSupportedPackageTypesApiRequest(
        SupplierCodeApi supplierCode,
        Set<PackageTypeApi> supportedPackageTypes
) {
}
