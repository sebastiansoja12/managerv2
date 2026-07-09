package com.warehouse.supplier.infrastructure.adapter.primary.dto;

public record SupplierBasicDataUpdateApiRequest(SupplierCodeApi supplierCode,
                                                String firstName,
                                                String lastName,
                                                String telephoneNumber) {
}
