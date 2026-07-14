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

import java.util.List;

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
    public List<Supplier> findAllByCurrentDepartment() {
        final Criteria<SupplierEntity> criteria = this.supplierBaseRepository.createCriteria(SupplierEntity.class);
        return criteria
                .list()
                .stream()
                .map(EntityToModelMapper::map)
                .toList();
    }

    @Override
    public Supplier findById(final SupplierId supplierId) {
        return this.supplierBaseRepository.createCriteria(SupplierEntity.class)
                .eq("supplierId.value", supplierId)
                .one()
                .map(EntityToModelMapper::map)
                .orElse(null);
    }

    @Override
    public Supplier findByCode(final SupplierCode supplierCode) {
        return this.supplierBaseRepository.createCriteria(SupplierEntity.class)
                .eq("supplierCode.value", supplierCode)
                .one()
                .map(EntityToModelMapper::map)
                .orElse(null);
    }
}
