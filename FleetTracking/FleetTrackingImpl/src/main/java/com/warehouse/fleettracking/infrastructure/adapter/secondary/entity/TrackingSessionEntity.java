package com.warehouse.fleettracking.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.TrackingSessionId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.fleettracking.domain.enumeration.TrackingSessionStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Entity(name = "fleettracking.TrackingSessionEntity")
@Table(name = "tracking_session")
public class TrackingSessionEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private TrackingSessionId trackingSessionId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "driver_user_id", nullable = false))
    private UserId driverUserId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "driver_username"))
    private Username driverUsername;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "device_id", nullable = false, length = 100))
    private DeviceId deviceId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "vehicle_id"))
    private VehicleId vehicleId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "department_code", length = 40))
    private DepartmentCode departmentCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TrackingSessionStatus status;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "sequence", column = @Column(name = "last_sequence")),
            @AttributeOverride(name = "latitude", column = @Column(name = "last_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "last_longitude")),
            @AttributeOverride(name = "accuracyMeters", column = @Column(name = "last_accuracy_meters")),
            @AttributeOverride(name = "speedMetersPerSecond", column = @Column(name = "last_speed_meters_per_second")),
            @AttributeOverride(name = "headingDegrees", column = @Column(name = "last_heading_degrees")),
            @AttributeOverride(name = "recordedAt", column = @Column(name = "last_recorded_at"))
    })
    private PositionEmbeddable lastPosition;

    public TrackingSessionEntity(final TrackingSessionId trackingSessionId,
                                 final UserId driverUserId,
                                 final Username driverUsername,
                                 final DeviceId deviceId,
                                 final VehicleId vehicleId,
                                 final DepartmentCode departmentCode,
                                 final TrackingSessionStatus status,
                                 final Instant startedAt,
                                 final Instant finishedAt,
                                 final PositionEmbeddable lastPosition) {
        this.trackingSessionId = trackingSessionId;
        this.driverUserId = driverUserId;
        this.driverUsername = driverUsername;
        this.deviceId = deviceId;
        this.vehicleId = vehicleId;
        this.departmentCode = departmentCode;
        this.status = status;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.lastPosition = lastPosition;
    }
}
