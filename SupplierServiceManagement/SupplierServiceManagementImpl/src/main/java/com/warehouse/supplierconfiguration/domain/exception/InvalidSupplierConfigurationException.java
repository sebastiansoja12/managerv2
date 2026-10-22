package com.warehouse.supplierconfiguration.domain.exception;

import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationViolation;

import java.util.List;

public class InvalidSupplierConfigurationException extends RuntimeException {

    private final List<SupplierConfigurationViolation> violations;

    public InvalidSupplierConfigurationException(final List<SupplierConfigurationViolation> violations) {
        super("Supplier configuration is invalid");
        this.violations = List.copyOf(violations);
    }

    public List<SupplierConfigurationViolation> getViolations() {
        return violations;
    }
}
