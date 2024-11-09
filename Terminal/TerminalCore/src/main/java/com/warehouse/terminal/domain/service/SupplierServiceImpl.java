package com.warehouse.terminal.domain.service;

import com.warehouse.terminal.domain.port.secondary.SupplierRepository;
import com.warehouse.terminal.domain.vo.Supplier;

public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(final SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Supplier findBySupplierCode(final String supplierCode) {
        return null;
    }
}
