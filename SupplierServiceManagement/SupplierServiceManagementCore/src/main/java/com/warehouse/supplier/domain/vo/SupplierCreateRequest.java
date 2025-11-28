package com.warehouse.supplier.domain.vo;

import com.warehouse.commonassets.identificator.SupplierCode;

public record SupplierCreateRequest(
        SupplierCode supplierCode,
        String firstName,
        String lastName,
        String telephoneNumber) {}

