package com.warehouse.terminal.request;

import java.time.Instant;

public record DeviceLocationRequestDto(
        Double latitude,
        Double longitude,
        Double altitude,
        Float accuracyMeters,
        String lastKnownAddress,
        String geoZone,
        Boolean gpsEnabled,
        Instant lastLocationUpdateAt
) {
}
