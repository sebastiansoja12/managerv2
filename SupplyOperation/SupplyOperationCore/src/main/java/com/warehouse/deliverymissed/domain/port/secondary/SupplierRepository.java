package com.warehouse.deliverymissed.domain.port.secondary;

public interface SupplierRepository {
    boolean validBySupplierCode(String supplierCode);
}
