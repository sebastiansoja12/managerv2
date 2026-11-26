package com.warehouse.supplierconfiguration.application.service;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierDirectoryServicePort;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierProfile;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierConfigurationParameter;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierConfigurationViolationCode;
import com.warehouse.supplierconfiguration.domain.exception.InvalidSupplierConfigurationException;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationViolation;

import java.util.List;
import java.util.Optional;

public class SubstituteSupplierService {

    private final SupplierDirectoryServicePort supplierDirectoryServicePort;

    public SubstituteSupplierService(final SupplierDirectoryServicePort supplierDirectoryServicePort) {
        this.supplierDirectoryServicePort = supplierDirectoryServicePort;
    }

    public SupplierId resolveSubstituteId(final SupplierCode substituteSupplierCode) {
        if (substituteSupplierCode == null || substituteSupplierCode.value() == null
                || substituteSupplierCode.value().isBlank()) {
            return null;
        }
        return this.supplierDirectoryServicePort.findBySupplierCode(substituteSupplierCode)
                .filter(SupplierProfile::active)
                .map(SupplierProfile::supplierId)
                .orElseThrow(() -> new InvalidSupplierConfigurationException(List.of(new SupplierConfigurationViolation(
                        SupplierConfigurationParameter.SUBSTITUTE_SUPPLIER_ID.name(),
                        SupplierConfigurationViolationCode.SUBSTITUTE_UNAVAILABLE))));
    }

    public Optional<SupplierCode> findSubstituteCode(final SupplierId substituteSupplierId) {
        return Optional.ofNullable(substituteSupplierId)
                .flatMap(this.supplierDirectoryServicePort::findBySupplierId)
                .map(SupplierProfile::supplierCode);
    }

    public List<SupplierProfile> findCandidates(final SupplierId supplierId) {
        return this.supplierDirectoryServicePort.findActiveSuppliers()
                .stream()
                .filter(candidate -> !candidate.supplierId().equals(supplierId))
                .toList();
    }
}
