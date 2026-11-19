package com.warehouse.supplierconfiguration.application.service;

import com.warehouse.supplierconfiguration.application.exception.SupplierConfigurationAccessDeniedException;
import com.warehouse.supplierconfiguration.application.exception.SupplierConfigurationNotFoundException;
import com.warehouse.supplierconfiguration.application.port.primary.command.SupplierConfigurationTarget;
import com.warehouse.supplierconfiguration.application.port.secondary.CurrentUserServicePort;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierDirectoryServicePort;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierProfile;

public class SupplierResolutionService {

    private final SupplierDirectoryServicePort supplierDirectoryServicePort;
    private final CurrentUserServicePort currentUserServicePort;

    public SupplierResolutionService(final SupplierDirectoryServicePort supplierDirectoryServicePort,
                                     final CurrentUserServicePort currentUserServicePort) {
        this.supplierDirectoryServicePort = supplierDirectoryServicePort;
        this.currentUserServicePort = currentUserServicePort;
    }

    public SupplierProfile resolve(final SupplierConfigurationTarget target) {
        return switch (target) {
            case SupplierConfigurationTarget.CurrentSupplier currentSupplier -> this.supplierDirectoryServicePort
                    .findByUserId(this.currentUserServicePort.getCurrentUserId())
                    .orElseThrow(() -> new SupplierConfigurationNotFoundException(
                            "Current user is not assigned to any supplier"));
            case SupplierConfigurationTarget.ManagedSupplier managedSupplier -> {
                final SupplierProfile supplier = this.supplierDirectoryServicePort
                        .findBySupplierCode(managedSupplier.supplierCode())
                        .orElseThrow(() -> new SupplierConfigurationNotFoundException(
                                "Supplier not found: " + managedSupplier.supplierCode().value()));
                if (!this.currentUserServicePort.canAccessDepartment(supplier.departmentId())) {
                    throw new SupplierConfigurationAccessDeniedException(
                            "Supplier is outside of the current user organizational scope");
                }
                yield supplier;
            }
        };
    }
}
