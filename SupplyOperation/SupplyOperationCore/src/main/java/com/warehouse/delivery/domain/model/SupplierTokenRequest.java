package com.warehouse.delivery.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SupplierTokenRequest {
    String supplierCode;
    UUID requestId;
}
