package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.port.secondary.SupplierNoDepartmentContextRepository;
import com.warehouse.supplier.infrastructure.adapter.secondary.entity.SupplierEntity;
import com.warehouse.supplier.infrastructure.adapter.secondary.mapper.EntityToModelMapper;
import com.warehouse.supplier.infrastructure.adapter.secondary.mapper.ModelToEntityMapper;

import java.util.List;

public class SupplierNoDepartmentContextRepositoryImpl implements SupplierNoDepartmentContextRepository {

    private final SupplierReadRepository supplierReadRepository;

    public SupplierNoDepartmentContextRepositoryImpl(final SupplierReadRepository supplierReadRepository) {
        this.supplierReadRepository = supplierReadRepository;
    }

    @Override
    public List<Supplier> findAll() {
        return supplierReadRepository.findAll()
                .stream()
                .map(EntityToModelMapper::map)
                .toList();
    }

    @Override
    public void update(final Supplier supplier) {
        final SupplierEntity supplierEntity = ModelToEntityMapper.map(supplier);
        this.supplierReadRepository.save(supplierEntity);
    }
}
