package com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto;

import java.util.Map;

public record SupplierConfigurationUpdateApiRequest(
        Long version,
        Map<String, Boolean> settings,
        Integer maxDeliveryAttempts,
        Integer maxShipmentsPerRoute,
        String substituteSupplierCode,
        String preferredLabelFormat,
        String defaultShipmentSortOrder) {
}
