package com.warehouse.logistics.domain.port.secondary;

public interface SupplierRepository {
    boolean validBySupplierCode(final String supplierCode);
}
