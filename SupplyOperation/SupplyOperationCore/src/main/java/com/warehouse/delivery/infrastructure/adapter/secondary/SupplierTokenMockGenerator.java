package com.warehouse.delivery.infrastructure.adapter.secondary;

import com.warehouse.delivery.domain.model.SupplierTokenRequest;

public interface SupplierTokenMockGenerator {
    String generateToken(SupplierTokenRequest request);
}
