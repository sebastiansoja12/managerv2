package com.warehouse.suppliertoken.domain.port.primary;


import com.warehouse.suppliertoken.domain.model.SupplierTokenRequest;
import com.warehouse.suppliertoken.domain.model.SupplierTokenResponse;
import com.warehouse.suppliertoken.domain.service.SupplierService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SupplierTokenPortImpl implements SupplierTokenPort {

    private final SupplierService service;

    @Override
    public SupplierTokenResponse protect(SupplierTokenRequest request) {
        return null;
    }
}
