package com.warehouse.delivery.infrastructure.adapter.secondary;

import com.warehouse.delivery.domain.model.DeliveryTokenRequest;

public interface SupplierTokenMockGenerator {
    String generateToken(DeliveryTokenRequest request);
}
