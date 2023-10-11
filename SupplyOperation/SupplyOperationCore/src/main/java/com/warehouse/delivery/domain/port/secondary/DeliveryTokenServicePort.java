package com.warehouse.delivery.domain.port.secondary;

import com.warehouse.delivery.domain.model.DeliveryTokenRequest;
import com.warehouse.delivery.domain.model.SupplierTokenResponse;

public interface DeliveryTokenServicePort {
    SupplierTokenResponse protect(DeliveryTokenRequest request);
}
