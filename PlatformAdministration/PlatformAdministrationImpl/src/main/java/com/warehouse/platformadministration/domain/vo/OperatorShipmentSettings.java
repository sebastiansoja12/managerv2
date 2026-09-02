package com.warehouse.platformadministration.domain.vo;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public record OperatorShipmentSettings(Map<String, Object> values) {

    public OperatorShipmentSettings {
        values = values == null ? Map.of() : Collections.unmodifiableMap(new LinkedHashMap<>(values));
    }
}
