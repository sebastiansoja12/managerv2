package com.warehouse.fleettracking.infrastructure.adapter.primary.api;

import com.warehouse.fleettracking.domain.enumeration.TrackingSessionStatus;

import java.time.Instant;

public record TrackingSessionApiResponse(TrackingSessionIdDto trackingSessionId,
                                         UserIdDto driverUserId,
                                         String driverUsername,
                                         String deviceId,
                                         VehicleIdDto vehicleId,
                                         String departmentCode,
                                         TrackingSessionStatus status,
                                         Instant startedAt,
                                         Instant finishedAt,
                                         PositionFixApiResponse lastPosition) {
}
