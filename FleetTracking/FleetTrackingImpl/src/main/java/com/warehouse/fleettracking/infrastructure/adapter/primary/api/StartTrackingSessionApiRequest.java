package com.warehouse.fleettracking.infrastructure.adapter.primary.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record StartTrackingSessionApiRequest(@NotNull @Valid DeviceIdDto deviceId, VehicleIdDto vehicleId) {
}
