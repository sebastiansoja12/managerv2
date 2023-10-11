package com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto;

import lombok.Value;

import java.util.UUID;

@Value
public class SupplierSignatureDto {
    String supplierCode;
    UUID deliveryId;
    String token;
}
