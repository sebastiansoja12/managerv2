package com.warehouse.infrastructure.event;

import java.time.Instant;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.infrastructure.dto.GeocodingConfigurationCreateDto;

public record OperatorGeocodingConfigurationCreateEvent(
        OperatorId operatorId,
        GeocodingConfigurationCreateDto configuration,
        Instant timestamp
) {
}
