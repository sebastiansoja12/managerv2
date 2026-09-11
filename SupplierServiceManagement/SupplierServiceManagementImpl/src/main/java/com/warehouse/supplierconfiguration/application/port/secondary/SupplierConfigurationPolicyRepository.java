package com.warehouse.supplierconfiguration.application.port.secondary;

import com.warehouse.supplierconfiguration.domain.model.SupplierConfigurationPolicy;

import java.util.Optional;

public interface SupplierConfigurationPolicyRepository {
    Optional<SupplierConfigurationPolicy> findCurrent();
    SupplierConfigurationPolicy save(final SupplierConfigurationPolicy policy);
}
