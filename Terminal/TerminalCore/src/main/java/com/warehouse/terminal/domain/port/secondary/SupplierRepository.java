package com.warehouse.terminal.domain.port.secondary;

import com.warehouse.terminal.domain.vo.Supplier;

public interface SupplierRepository {
    Supplier findBySupplierCode(final String supplierCode);
}
