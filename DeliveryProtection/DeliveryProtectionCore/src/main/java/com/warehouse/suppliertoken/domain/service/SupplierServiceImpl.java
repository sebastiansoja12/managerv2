package com.warehouse.suppliertoken.domain.service;


import com.warehouse.suppliertoken.domain.model.SupplierTokenRequest;
import com.warehouse.suppliertoken.domain.model.SupplierTokenResponse;
import com.warehouse.suppliertoken.domain.port.secondary.SupplierTokenServicePort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierTokenServicePort supplierTokenServicePort;
    @Override
    public SupplierTokenResponse protect(SupplierTokenRequest request) {
        return null;
    }
}
