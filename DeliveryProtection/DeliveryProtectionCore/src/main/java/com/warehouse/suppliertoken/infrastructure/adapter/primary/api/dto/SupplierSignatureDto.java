package com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto;

import lombok.Value;

import java.util.UUID;

@Value
public class SupplierSignatureDto {
    String supplierCode;
    UUID id;
    String token;
}
