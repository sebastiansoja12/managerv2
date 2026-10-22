package com.warehouse.supplierconfiguration.domain.vo;

import java.util.UUID;

public record SupplierConfigurationPolicyId(UUID value) {

    public static SupplierConfigurationPolicyId generate() {
        return new SupplierConfigurationPolicyId(UUID.randomUUID());
    }
}
