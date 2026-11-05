package com.warehouse.fleettracking.infrastructure.adapter.primary.api;

import java.time.Instant;

public record PositionFixApiResponse(long sequence,
                                     double latitude,
                                     double longitude,
                                     Double accuracyMeters,
                                     Double speedMetersPerSecond,
                                     Double headingDegrees,
                                     Instant recordedAt) {
}
