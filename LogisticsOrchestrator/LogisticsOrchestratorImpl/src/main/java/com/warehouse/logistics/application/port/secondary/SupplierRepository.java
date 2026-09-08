package com.warehouse.logistics.application.port.secondary;

public interface SupplierRepository {
    boolean validBySupplierCode(final String supplierCode);
}
