package com.warehouse.suppliertoken.domain.port.secondary;

import com.warehouse.suppliertoken.domain.model.SupplierTokenRequest;
import com.warehouse.suppliertoken.domain.model.SupplierTokenResponse;

public interface SupplierTokenServicePort {
    SupplierTokenResponse protect(SupplierTokenRequest request);
}
