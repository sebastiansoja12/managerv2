package com.warehouse.fleettracking.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Embeddable
public class PositionEmbeddable {

    @Column(name = "sequence")
    private Long sequence;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "accuracy_meters")
    private Double accuracyMeters;

    @Column(name = "speed_meters_per_second")
    private Double speedMetersPerSecond;

    @Column(name = "heading_degrees")
    private Double headingDegrees;

    @Column(name = "recorded_at")
    private Instant recordedAt;

    public PositionEmbeddable(final Long sequence,
                              final Double latitude,
                              final Double longitude,
                              final Double accuracyMeters,
                              final Double speedMetersPerSecond,
                              final Double headingDegrees,
                              final Instant recordedAt) {
        this.sequence = sequence;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracyMeters = accuracyMeters;
        this.speedMetersPerSecond = speedMetersPerSecond;
        this.headingDegrees = headingDegrees;
        this.recordedAt = recordedAt;
    }
}
