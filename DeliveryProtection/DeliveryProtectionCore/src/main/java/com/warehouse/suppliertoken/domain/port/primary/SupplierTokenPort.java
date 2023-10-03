package com.warehouse.suppliertoken.domain.port.primary;

import com.warehouse.suppliertoken.domain.model.SupplierTokenRequest;
import com.warehouse.suppliertoken.domain.model.SupplierTokenResponse;

public interface SupplierTokenPort {
    SupplierTokenResponse protect(SupplierTokenRequest request);
}
