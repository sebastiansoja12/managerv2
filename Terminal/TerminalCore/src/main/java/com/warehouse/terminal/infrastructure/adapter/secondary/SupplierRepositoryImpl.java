package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.terminal.domain.port.secondary.SupplierRepository;
import com.warehouse.terminal.domain.vo.Supplier;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.SupplierEntity;

import java.util.Optional;

public class SupplierRepositoryImpl implements SupplierRepository {

    private final SupplierReadRepository repository;

    public SupplierRepositoryImpl(final SupplierReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Supplier findBySupplierCode(final String supplierCode) {
        final Optional<SupplierEntity> supplier = this.repository.findBySupplierCode(supplierCode);
        return null;
    }
}
