package com.warehouse.supplierconfiguration.application.service;

import com.warehouse.supplierconfiguration.application.port.primary.result.SupplierConfigurationResult;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierProfile;
import com.warehouse.supplierconfiguration.domain.model.SupplierConfiguration;
import com.warehouse.supplierconfiguration.domain.model.SupplierConfigurationPolicy;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationSnapshot;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationValues;

public class SupplierConfigurationResultFactory {

    private final SubstituteSupplierService substituteSupplierService;

    public SupplierConfigurationResultFactory(final SubstituteSupplierService substituteSupplierService) {
        this.substituteSupplierService = substituteSupplierService;
    }

    public SupplierConfigurationResult create(final SupplierProfile supplier,
                                              final SupplierConfiguration configuration,
                                              final SupplierConfigurationPolicy policy) {
        final SupplierConfigurationValues effectiveValues = configuration.effectiveValues(policy);
        final SupplierConfigurationSnapshot snapshot = configuration.snapshot();
        return new SupplierConfigurationResult(
                supplier.supplierId(),
                supplier.supplierCode(),
                supplier.firstName(),
                supplier.lastName(),
                effectiveValues,
                this.substituteSupplierService.findSubstituteCode(effectiveValues.substituteSupplierId()).orElse(null),
                policy.getEnforcedSettings(),
                snapshot.version(),
                snapshot.updatedAt(),
                snapshot.updatedBy());
    }
}
