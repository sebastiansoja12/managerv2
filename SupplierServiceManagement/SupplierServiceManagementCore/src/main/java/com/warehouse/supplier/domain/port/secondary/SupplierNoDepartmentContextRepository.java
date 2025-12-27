package com.warehouse.supplier.domain.port.secondary;

import com.warehouse.supplier.domain.model.Supplier;

import java.util.List;

public interface SupplierNoDepartmentContextRepository {
    List<Supplier> findAll();

    void update(final Supplier supplier);
}
