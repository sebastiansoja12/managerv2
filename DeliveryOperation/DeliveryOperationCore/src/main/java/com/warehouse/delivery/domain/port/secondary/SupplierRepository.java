package com.warehouse.delivery.domain.port.secondary;

public interface SupplierRepository {
    boolean validBySupplierCode(final String supplierCode);
}
