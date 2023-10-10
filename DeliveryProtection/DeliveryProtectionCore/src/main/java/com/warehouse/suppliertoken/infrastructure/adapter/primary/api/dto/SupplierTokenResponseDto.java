package com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto;

import lombok.Value;

import java.util.List;

@Value
public class SupplierTokenResponseDto {
    List<SupplierSignatureDto> supplierSignature;
}
