package com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SupplierConfigurationRevisionApi(
        UUID revisionId,
        String changeType,
        Long changedBy,
        Instant changedAt,
        List<SupplierConfigurationChangeApi> changes) {

    public record SupplierConfigurationChangeApi(String field, String previousValue, String newValue) {
    }
}
