package com.warehouse.supplierconfiguration.application.port.secondary;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplierconfiguration.domain.model.SupplierConfiguration;

import java.util.List;
import java.util.Optional;

public interface SupplierConfigurationRepository {
    Optional<SupplierConfiguration> findBySupplierId(final SupplierId supplierId);
    SupplierConfiguration save(final SupplierConfiguration supplierConfiguration);

    List<SupplierConfiguration> findBySubstituteSupplierId(final SupplierId substituteSupplierId);

    void deleteBySupplierId(final SupplierId supplierId);
}
