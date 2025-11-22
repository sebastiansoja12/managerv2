package com.warehouse.supplier.domain.service;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.port.secondary.SupplierRepository;

import java.util.UUID;


public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(final SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public void create(final Supplier supplier) {
        this.supplierRepository.createOrUpdate(supplier);
    }

    @Override
    public Supplier findById(final SupplierId supplierId) {
        return supplierRepository.findById(supplierId);
    }

    @Override
    public Supplier findByCode(final SupplierCode supplierCode) {
        return supplierRepository.findByCode(supplierCode);
    }

    @Override
    public SupplierId nextSupplierId() {
        return new SupplierId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }
}
