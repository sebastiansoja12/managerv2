package com.warehouse.delivery.infrastructure.adapter.secondary;

import com.warehouse.delivery.domain.port.secondary.SupplierRepository;
import com.warehouse.delivery.infrastructure.adapter.secondary.entity.SupplierEntity;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class SupplierRepositoryImpl implements SupplierRepository {

    private final SupplierReadRepository repository;

    @Override
    public boolean validBySupplierCode(final String supplierCode) {
        final SupplierEntity supplierEntity = repository.findBySupplierCode(supplierCode).orElse(null);
        return supplierEntity != null && supplierEntity.isActive();
    }

}
