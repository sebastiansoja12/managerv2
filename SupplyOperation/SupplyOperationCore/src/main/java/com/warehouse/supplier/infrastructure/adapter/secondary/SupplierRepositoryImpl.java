package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.port.secondary.SupplierRepository;
import com.warehouse.supplier.infrastructure.adapter.secondary.entity.SupplierEntity;
import com.warehouse.supplier.infrastructure.adapter.secondary.mapper.DepotEntityMapper;
import com.warehouse.supplier.infrastructure.adapter.secondary.mapper.SupplierEntityMapper;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SupplierRepositoryImpl implements SupplierRepository {

    private final SupplierEntityMapper supplierEntityMapper;

    private final DepotEntityMapper depotEntityMapper;

    private final SupplierReadRepository supplierReadRepository;

    @Override
    public void create(Supplier supplier) {
        final SupplierEntity supplierEntity = supplierEntityMapper.map(supplier);
        supplierReadRepository.save(supplierEntity);
    }

    @Override
    public List<Supplier> findAll() {
        final List<SupplierEntity> supplierEntities = supplierReadRepository.findAll();
        return supplierEntityMapper.map(supplierEntities);
    }

    @Override
    public void createMultipleSuppliers(List<Supplier> suppliers) {
        final List<SupplierEntity> supplierEntities = supplierEntityMapper.mapToListEntity(suppliers);
        supplierReadRepository.saveAll(supplierEntities);
    }

    @Override
    public List<Supplier> findByDepotCode(String depotCode) {
        final List<SupplierEntity> supplierEntities = supplierReadRepository.findByDepot_DepotCode(depotCode);
        return supplierEntityMapper.map(supplierEntities);
    }

    @Override
    public Supplier findByCode(String supplierCode) {
        final SupplierEntity supplierEntity = supplierReadRepository.findBySupplierCode(supplierCode).orElseThrow();
        return supplierEntityMapper.map(supplierEntity);
    }
}
