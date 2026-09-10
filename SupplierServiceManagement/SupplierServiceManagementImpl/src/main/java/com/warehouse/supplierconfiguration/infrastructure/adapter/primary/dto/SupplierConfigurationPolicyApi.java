package com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto;

import java.time.Instant;
import java.util.Map;

public record SupplierConfigurationPolicyApi(
        Map<String, Boolean> enforcedSettings,
        Long version,
        Instant updatedAt,
        Long updatedBy) {
}
