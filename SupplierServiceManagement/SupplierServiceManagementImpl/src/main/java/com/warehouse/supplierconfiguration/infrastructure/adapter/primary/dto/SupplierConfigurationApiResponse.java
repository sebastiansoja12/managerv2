package com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record SupplierConfigurationApiResponse(
        Long supplierId,
        String supplierCode,
        String firstName,
        String lastName,
        Map<String, Boolean> settings,
        Integer maxDeliveryAttempts,
        Integer maxShipmentsPerRoute,
        String substituteSupplierCode,
        String preferredLabelFormat,
        String defaultShipmentSortOrder,
        Map<String, Boolean> enforcedSettings,
        List<SupplierSettingDefinitionApi> definitions,
        Long version,
        Instant updatedAt,
        Long updatedBy) {

    public record SupplierSettingDefinitionApi(String setting, String group, String category, boolean defaultValue) {
    }
}
