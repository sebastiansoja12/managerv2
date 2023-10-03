package com.warehouse.delivery.infrastructure.adapter.secondary;

import com.warehouse.delivery.domain.model.SupplierSignature;
import com.warehouse.delivery.domain.model.SupplierTokenRequest;
import com.warehouse.delivery.domain.model.SupplierTokenResponse;

import com.warehouse.delivery.domain.port.secondary.SupplierTokenServicePort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SupplierTokenMockAdapter implements SupplierTokenServicePort {

    private final SupplierTokenMockGenerator supplierTokenMockGenerator;

    @Override
    public SupplierTokenResponse protect(SupplierTokenRequest request) {
        final SupplierSignature signature = SupplierSignature.builder()
                .id(request.getRequestId())
                .supplierCode(request.getSupplierCode())
                .build();

        signature.setToken(supplierTokenMockGenerator.generateToken(request));

        return new SupplierTokenResponse(signature);
    }
}
