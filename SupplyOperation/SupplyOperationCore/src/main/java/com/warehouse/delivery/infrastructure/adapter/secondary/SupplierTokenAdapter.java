package com.warehouse.delivery.infrastructure.adapter.secondary;

import org.springframework.web.client.support.RestGatewaySupport;

import com.warehouse.delivery.domain.model.SupplierTokenRequest;
import com.warehouse.delivery.domain.model.SupplierTokenResponse;
import com.warehouse.delivery.domain.port.secondary.SupplierTokenServicePort;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SupplierTokenAdapter extends RestGatewaySupport implements SupplierTokenServicePort {

    @Override
    public SupplierTokenResponse sign(SupplierTokenRequest request) {
        // TODO INPL-6150
        return null;
    }
}
