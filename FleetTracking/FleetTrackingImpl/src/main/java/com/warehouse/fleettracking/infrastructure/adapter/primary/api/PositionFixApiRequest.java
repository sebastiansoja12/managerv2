package com.warehouse.fleettracking.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.Instant;

public record PositionFixApiRequest(@NotNull @PositiveOrZero Long sequence,
                                    @NotNull Double latitude,
                                    @NotNull Double longitude,
                                    Double accuracyMeters,
                                    Double speedMetersPerSecond,
                                    Double headingDegrees,
                                    @NotNull Instant recordedAt) {
}
