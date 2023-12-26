package com.warehouse.supplier.infrastructure.adapter.secondary;

import java.util.List;

import com.warehouse.supplier.domain.model.SupplierModelRequest;
import com.warehouse.supplier.domain.vo.SupplierModelResponse;
import com.warehouse.supplier.domain.port.secondary.SupplierServicePort;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SupplierAdapter implements SupplierServicePort {

    // TODO INPL-6151
    // send supplier request to STS to save in liquibase
    @Override
    public List<SupplierModelResponse> createSuppliers(List<SupplierModelRequest> suppliers) {
        return null;
    }
}
