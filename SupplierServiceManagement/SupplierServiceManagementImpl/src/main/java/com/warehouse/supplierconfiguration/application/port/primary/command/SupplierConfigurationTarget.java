package com.warehouse.supplierconfiguration.application.port.primary.command;

import com.warehouse.commonassets.identificator.SupplierCode;

public sealed interface SupplierConfigurationTarget {

    static SupplierConfigurationTarget currentSupplier() {
        return new CurrentSupplier();
    }

    static SupplierConfigurationTarget managedSupplier(final SupplierCode supplierCode) {
        return new ManagedSupplier(supplierCode);
    }

    record CurrentSupplier() implements SupplierConfigurationTarget {
    }

    record ManagedSupplier(SupplierCode supplierCode) implements SupplierConfigurationTarget {
    }
}
