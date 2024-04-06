package com.warehouse.deliverymissed.infrastructure.adapter.secondary;

import com.warehouse.deliverymissed.domain.port.secondary.SupplierRepository;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.SupplierEntity;
import lombok.AllArgsConstructor;

import java.util.Optional;


@AllArgsConstructor
public class SupplierRepositoryImpl implements SupplierRepository {

    private final SupplierReadRepository repository;

    @Override
    public boolean validBySupplierCode(String supplierCode) {
        final Optional<SupplierEntity> supplierEntity = repository.findBySupplierCode(supplierCode);
        return supplierEntity.map(this::validateSupplier).orElseThrow();
    }

    private boolean validateSupplier(SupplierEntity supplierEntity) {
        return supplierEntity.getActive();
    }
}
