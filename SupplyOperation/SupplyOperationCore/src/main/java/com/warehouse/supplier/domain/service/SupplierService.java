package com.warehouse.supplier.domain.service;

import com.warehouse.supplier.domain.model.Supplier;

import java.util.List;

public interface SupplierService {
    Supplier create(Supplier supplier);

    List<Supplier> findAll();

    Supplier update(Supplier supplier);

    List<Supplier> createMultipleSuppliers(List<Supplier> suppliers);

    List<Supplier> findSuppliersByDepotCode(String depotCode);

    Supplier findSupplierByCode(String supplierCode);
}
