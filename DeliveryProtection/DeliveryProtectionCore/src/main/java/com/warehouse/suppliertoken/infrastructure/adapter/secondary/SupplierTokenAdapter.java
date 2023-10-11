package com.warehouse.suppliertoken.infrastructure.adapter.secondary;

import com.warehouse.suppliertoken.domain.model.SupplierTokenRequest;
import com.warehouse.suppliertoken.domain.model.SupplierTokenResponse;
import com.warehouse.suppliertoken.domain.port.secondary.SupplierTokenServicePort;
import lombok.AllArgsConstructor;
import org.springframework.web.client.support.RestGatewaySupport;


@AllArgsConstructor
public class SupplierTokenAdapter extends RestGatewaySupport implements SupplierTokenServicePort {

    @Override
    public SupplierTokenResponse protect(SupplierTokenRequest request) {
        // TODO INPL-6151
        return null;
    }
}