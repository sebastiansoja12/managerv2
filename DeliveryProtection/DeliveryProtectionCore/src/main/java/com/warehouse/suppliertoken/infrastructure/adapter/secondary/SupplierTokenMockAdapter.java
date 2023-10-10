package com.warehouse.suppliertoken.infrastructure.adapter.secondary;

import com.warehouse.suppliertoken.domain.model.SupplierTokenRequest;
import com.warehouse.suppliertoken.domain.model.SupplierTokenResponse;
import com.warehouse.suppliertoken.domain.port.secondary.SupplierTokenServicePort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SupplierTokenMockAdapter implements SupplierTokenServicePort {
    @Override
    public SupplierTokenResponse protect(SupplierTokenRequest request) {
        return null;
    }
}
