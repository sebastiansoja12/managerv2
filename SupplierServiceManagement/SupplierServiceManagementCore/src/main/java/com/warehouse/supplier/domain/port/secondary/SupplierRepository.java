package com.warehouse.supplier.domain.port.secondary;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplier.domain.model.Supplier;

public interface SupplierRepository {

    void createOrUpdate(final Supplier supplier);
    Supplier findById(final SupplierId supplierId);
    Supplier findByCode(final SupplierCode supplierCode);
}
