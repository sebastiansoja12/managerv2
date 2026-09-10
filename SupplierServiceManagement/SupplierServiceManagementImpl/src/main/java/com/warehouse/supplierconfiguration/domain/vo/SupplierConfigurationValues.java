package com.warehouse.supplierconfiguration.domain.vo;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplierconfiguration.domain.enumeration.LabelFormat;
import com.warehouse.supplierconfiguration.domain.enumeration.ShipmentSortOrder;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierSetting;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public record SupplierConfigurationValues(Map<SupplierSetting, Boolean> settings,
                                          Integer maxDeliveryAttempts,
                                          Integer maxShipmentsPerRoute,
                                          SupplierId substituteSupplierId,
                                          LabelFormat preferredLabelFormat,
                                          ShipmentSortOrder defaultShipmentSortOrder) {

    public static final int DEFAULT_MAX_DELIVERY_ATTEMPTS = 3;

    public SupplierConfigurationValues {
        final EnumMap<SupplierSetting, Boolean> copiedSettings = new EnumMap<>(SupplierSetting.class);
        copiedSettings.putAll(settings);
        settings = Collections.unmodifiableMap(copiedSettings);
    }

    public static SupplierConfigurationValues defaults() {
        final EnumMap<SupplierSetting, Boolean> defaultSettings = new EnumMap<>(SupplierSetting.class);
        Arrays.stream(SupplierSetting.values())
                .forEach(setting -> defaultSettings.put(setting, setting.getDefaultValue()));
        return new SupplierConfigurationValues(defaultSettings, DEFAULT_MAX_DELIVERY_ATTEMPTS, null, null,
                LabelFormat.PDF_A6, ShipmentSortOrder.DELIVERY_ORDER);
    }

    public boolean isEnabled(final SupplierSetting setting) {
        return Boolean.TRUE.equals(settings.get(setting));
    }

    public SupplierConfigurationValues withSettings(final Map<SupplierSetting, Boolean> changedSettings) {
        final EnumMap<SupplierSetting, Boolean> mergedSettings = new EnumMap<>(SupplierSetting.class);
        mergedSettings.putAll(settings);
        mergedSettings.putAll(changedSettings);
        return new SupplierConfigurationValues(mergedSettings, maxDeliveryAttempts, maxShipmentsPerRoute,
                substituteSupplierId, preferredLabelFormat, defaultShipmentSortOrder);
    }
}
