package com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto;

import lombok.Value;

import java.util.UUID;

@Value
public class SupplierSignatureDto {
    UUID deliveryId;
    String token;
}
