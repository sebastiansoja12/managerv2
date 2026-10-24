package com.warehouse.supplier.api.dto;

import com.warehouse.commonassets.identificator.SupplierId;

import java.util.Map;

public record SupplierConfigurationDto(
        SupplierId supplierId,
        Map<String, Boolean> settings,
        Integer maxDeliveryAttempts,
        Integer maxShipmentsPerRoute,
        SupplierId substituteSupplierId,
        String preferredLabelFormat,
        String defaultShipmentSortOrder) {

    public boolean isEnabled(final String setting) {
        return Boolean.TRUE.equals(settings.get(setting));
    }
}
