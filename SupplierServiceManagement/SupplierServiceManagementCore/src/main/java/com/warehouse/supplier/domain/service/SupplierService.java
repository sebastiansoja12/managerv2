package com.warehouse.supplier.domain.service;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplier.domain.model.Supplier;

public interface SupplierService {
    void create(final Supplier supplier);
    Supplier findById(final SupplierId supplierId);
    Supplier findByCode(final SupplierCode supplierCode);
    SupplierId nextSupplierId();
}
