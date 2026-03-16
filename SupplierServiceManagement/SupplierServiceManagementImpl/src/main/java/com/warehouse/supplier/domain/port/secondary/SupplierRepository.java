package com.warehouse.supplier.domain.port.secondary;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplier.domain.model.Supplier;

import java.util.List;

public interface SupplierRepository {
    void create(final Supplier supplier);
    void update(final Supplier supplier);
    List<Supplier> findAllByCurrentDepartment();
    Supplier findById(final SupplierId supplierId);
    Supplier findByCode(final SupplierCode supplierCode);
}
