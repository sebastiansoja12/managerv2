package com.warehouse.supplier.infrastructure.adapter.primary.dto;

public record ChangeSupportedPackageTypesApiRequest(
        SupplierCodeApi supplierCode,
        String packageType
) {
}
