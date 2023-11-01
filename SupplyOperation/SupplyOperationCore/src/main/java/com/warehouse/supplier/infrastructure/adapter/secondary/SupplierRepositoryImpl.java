package com.warehouse.supplier.infrastructure.adapter.secondary;

import java.util.List;
import java.util.stream.Collectors;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.model.SupplierModelRequest;
import com.warehouse.supplier.domain.port.secondary.SupplierRepository;
import com.warehouse.supplier.infrastructure.adapter.secondary.entity.SupplierEntity;
import com.warehouse.supplier.infrastructure.adapter.secondary.exception.SupplierNotFoundException;
import com.warehouse.supplier.infrastructure.adapter.secondary.mapper.SupplierEntityMapper;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@AllArgsConstructor
public class SupplierRepositoryImpl implements SupplierRepository {

    private final SupplierEntityMapper supplierEntityMapper;

    private final SupplierReadRepository supplierReadRepository;

    @Override
    public Supplier create(Supplier supplier) {
        final SupplierEntity supplierEntity = supplierEntityMapper.map(supplier);
        supplierReadRepository.save(supplierEntity);
        return supplierEntityMapper.map(supplierEntity);
    }

    @Override
    @Cacheable("suppliersCache")
    public List<Supplier> findAll() {
        final List<SupplierEntity> supplierEntities = supplierReadRepository.findAll();
        return supplierEntityMapper.map(supplierEntities);
    }

    @Override
    public List<SupplierModelRequest> createMultipleSuppliers(List<Supplier> suppliers) {
        final List<SupplierEntity> supplierEntities = supplierEntityMapper.mapToListEntity(suppliers);
        supplierReadRepository.saveAll(supplierEntities);

        return supplierEntities.stream()
                .map(supplierEntityMapper::mapToModel)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable("suppliersDepotCodeCache")
    public List<Supplier> findByDepotCode(String depotCode) {
        final List<SupplierEntity> supplierEntities = supplierReadRepository.findByDepot_DepotCode(depotCode);
        return supplierEntityMapper.map(supplierEntities);
    }

    @Override
    @Cacheable("suppliersCodeCache")
    public List<Supplier> findBySupplierCode(String supplierCode) {
        final List<SupplierEntity> supplierEntities = supplierReadRepository.findAllBySupplierCode(supplierCode);
        return supplierEntities.stream()
                .map(supplierEntityMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable("supplierCodeCache")
    public Supplier findByCode(String supplierCode) {
        final SupplierEntity supplierEntity = supplierReadRepository.findBySupplierCode(supplierCode).orElseThrow(
                () -> new SupplierNotFoundException("Supplier was not found")
        );
        return supplierEntityMapper.map(supplierEntity);
    }
}
