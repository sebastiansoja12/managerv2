package com.warehouse.supplier.infrastructure.adapter.primary.dto;

import com.warehouse.commonassets.identificator.SupplierCode;

public record SupplierCreateApiRequest(SupplierCode supplierCode,
                                       String firstName,
                                       String lastName,
                                       String telephoneNumber) { }
