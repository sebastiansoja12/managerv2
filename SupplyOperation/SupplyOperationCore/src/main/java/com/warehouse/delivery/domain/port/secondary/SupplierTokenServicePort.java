package com.warehouse.delivery.domain.port.secondary;

import com.warehouse.delivery.domain.model.SupplierTokenRequest;
import com.warehouse.delivery.domain.model.SupplierTokenResponse;

public interface SupplierTokenServicePort {
    SupplierTokenResponse protect(SupplierTokenRequest request);
}
