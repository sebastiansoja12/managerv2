package com.warehouse.logistics.infrastructure.adapter.secondary;

import com.warehouse.logistics.domain.port.secondary.SupplierRepository;
import com.warehouse.logistics.infrastructure.adapter.secondary.entity.SupplierEntity;

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
