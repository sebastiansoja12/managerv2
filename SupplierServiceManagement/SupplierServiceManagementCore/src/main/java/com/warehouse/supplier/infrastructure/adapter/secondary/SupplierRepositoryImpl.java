package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.port.secondary.SupplierRepository;
import com.warehouse.supplier.infrastructure.adapter.secondary.entity.SupplierEntity;
import com.warehouse.supplier.infrastructure.adapter.secondary.mapper.EntityToModelMapper;
import com.warehouse.supplier.infrastructure.adapter.secondary.mapper.ModelToEntityMapper;

public class SupplierRepositoryImpl implements SupplierRepository {

    private final BaseRepository<SupplierEntity> supplierBaseRepository;

    public SupplierRepositoryImpl(final BaseRepository<SupplierEntity> supplierBaseRepository) {
        this.supplierBaseRepository = supplierBaseRepository;
    }

    @Override
    public void create(final Supplier supplier) {
        final SupplierEntity supplierEntity = ModelToEntityMapper.map(supplier);
        this.supplierBaseRepository.create(supplierEntity);
    }

    @Override
    public void update(final Supplier supplier) {
        final SupplierEntity supplierEntity = ModelToEntityMapper.map(supplier);
        this.supplierBaseRepository.update(supplierEntity);
    }

    @Override
    public Supplier findById(final SupplierId supplierId) {
        final Criteria<SupplierEntity> criteria = this.supplierBaseRepository.createCriteria(SupplierEntity.class);
        criteria.and("supplierId.value", supplierId);
        return criteria
                .getSingleResult()
                .map(EntityToModelMapper::map)
                .orElse(null);
    }

    @Override
    public Supplier findByCode(final SupplierCode supplierCode) {
        final Criteria<SupplierEntity> criteria = this.supplierBaseRepository.createCriteria(SupplierEntity.class);
        criteria.and("supplierCode.value", supplierCode);
        return criteria
                .getSingleResult()
                .map(EntityToModelMapper::map)
                .orElse(null);
    }
}
