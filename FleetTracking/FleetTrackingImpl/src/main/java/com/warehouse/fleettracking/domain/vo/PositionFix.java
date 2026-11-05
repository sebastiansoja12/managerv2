package com.warehouse.fleettracking.domain.vo;

import com.warehouse.fleettracking.domain.exception.FleetTrackingException;

import java.time.Duration;
import java.time.Instant;

public record PositionFix(long sequence,
                          double latitude,
                          double longitude,
                          Double accuracyMeters,
                          Double speedMetersPerSecond,
                          Double headingDegrees,
                          Instant recordedAt) {

    private static final double EARTH_RADIUS_METERS = 6_371_000.0;

    public PositionFix {
        if (sequence < 0) {
            throw invalid("Position sequence cannot be negative");
        }
        if (latitude < -90.0 || latitude > 90.0 || longitude < -180.0 || longitude > 180.0) {
            throw invalid("Position coordinates are out of range");
        }
        if (recordedAt == null) {
            throw invalid("Position record time cannot be null");
        }
    }

    public double distanceMetersTo(final PositionFix other) {
        final double latitudeDelta = Math.toRadians(other.latitude - this.latitude);
        final double longitudeDelta = Math.toRadians(other.longitude - this.longitude);
        final double haversine = Math.pow(Math.sin(latitudeDelta / 2), 2)
                + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude))
                * Math.pow(Math.sin(longitudeDelta / 2), 2);
        return 2 * EARTH_RADIUS_METERS * Math.asin(Math.sqrt(haversine));
    }

    public Duration timeSince(final PositionFix earlier) {
        return Duration.between(earlier.recordedAt, this.recordedAt);
    }

    private static FleetTrackingException invalid(final String message) {
        return new FleetTrackingException(FleetTrackingException.Reason.INVALID_REQUEST, message);
    }
}
