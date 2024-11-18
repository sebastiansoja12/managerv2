package com.warehouse.terminal.domain.service;

import com.warehouse.terminal.domain.vo.Supplier;

public interface SupplierService {
    Supplier findBySupplierCode(final String supplierCode);
}
