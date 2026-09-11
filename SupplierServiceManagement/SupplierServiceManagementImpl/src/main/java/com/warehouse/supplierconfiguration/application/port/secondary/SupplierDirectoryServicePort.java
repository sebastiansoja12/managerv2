package com.warehouse.supplierconfiguration.application.port.secondary;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;
import java.util.Optional;

public interface SupplierDirectoryServicePort {
    Optional<SupplierProfile> findBySupplierId(final SupplierId supplierId);
    Optional<SupplierProfile> findBySupplierCode(final SupplierCode supplierCode);
    Optional<SupplierProfile> findByUserId(final UserId userId);
    List<SupplierProfile> findActiveSuppliers();
}
