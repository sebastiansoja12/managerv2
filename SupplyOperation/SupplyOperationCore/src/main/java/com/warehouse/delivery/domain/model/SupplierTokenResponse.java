package com.warehouse.delivery.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SupplierTokenResponse {
    SupplierSignature supplierSignature;
}