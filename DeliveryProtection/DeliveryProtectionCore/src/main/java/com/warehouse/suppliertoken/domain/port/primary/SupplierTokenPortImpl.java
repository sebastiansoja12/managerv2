package com.warehouse.suppliertoken.domain.port.primary;


import com.warehouse.suppliertoken.domain.model.SupplierTokenRequest;
import com.warehouse.suppliertoken.domain.model.SupplierTokenResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SupplierTokenPortImpl implements SupplierTokenPort {

    @Override
    public SupplierTokenResponse protect(SupplierTokenRequest request) {
        return null;
    }
}
