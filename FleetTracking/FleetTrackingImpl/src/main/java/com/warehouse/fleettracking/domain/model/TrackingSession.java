package com.warehouse.fleettracking.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.TrackingSessionId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.fleettracking.domain.enumeration.TrackingSessionStatus;
import com.warehouse.fleettracking.domain.exception.FleetTrackingException;
import com.warehouse.fleettracking.domain.vo.PositionFix;
import com.warehouse.fleettracking.domain.vo.TrackingDriver;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class TrackingSession {

    private static final double MAX_ACCURACY_METERS = 100.0;

    private static final double MAX_SPEED_METERS_PER_SECOND = 70.0;

    private final TrackingSessionId trackingSessionId;

    private final TrackingDriver driver;

    private final DeviceId deviceId;

    private final VehicleId vehicleId;

    private final DepartmentCode departmentCode;

    private final Instant startedAt;

    private final List<PositionFix> acceptedPositions;

    private TrackingSessionStatus status;

    private PositionFix lastPosition;

    private Instant finishedAt;

    private TrackingSession(final TrackingSessionId trackingSessionId,
                            final TrackingDriver driver,
                            final DeviceId deviceId,
                            final VehicleId vehicleId,
                            final DepartmentCode departmentCode,
                            final Instant startedAt,
                            final TrackingSessionStatus status,
                            final PositionFix lastPosition,
                            final Instant finishedAt) {
        this.trackingSessionId = Objects.requireNonNull(trackingSessionId, "Tracking session ID cannot be null");
        this.driver = Objects.requireNonNull(driver, "Tracking session driver cannot be null");
        this.deviceId = Objects.requireNonNull(deviceId, "Tracking session device cannot be null");
        this.vehicleId = vehicleId;
        this.departmentCode = departmentCode;
        this.startedAt = Objects.requireNonNull(startedAt, "Tracking session start time cannot be null");
        this.status = Objects.requireNonNull(status, "Tracking session status cannot be null");
        this.lastPosition = lastPosition;
        this.finishedAt = finishedAt;
        this.acceptedPositions = new ArrayList<>();
    }

    public static TrackingSession start(final TrackingDriver driver,
                                        final DeviceId deviceId,
                                        final VehicleId vehicleId,
                                        final DepartmentCode departmentCode,
                                        final Instant startedAt) {
        return new TrackingSession(TrackingSessionId.nextId(), driver, deviceId, vehicleId, departmentCode, startedAt,
                TrackingSessionStatus.ACTIVE, null, null);
    }

    public static TrackingSession rehydrate(final TrackingSessionId trackingSessionId,
                                            final TrackingDriver driver,
                                            final DeviceId deviceId,
                                            final VehicleId vehicleId,
                                            final DepartmentCode departmentCode,
                                            final Instant startedAt,
                                            final TrackingSessionStatus status,
                                            final PositionFix lastPosition,
                                            final Instant finishedAt) {
        return new TrackingSession(trackingSessionId, driver, deviceId, vehicleId, departmentCode, startedAt, status,
                lastPosition, finishedAt);
    }

    public void report(final UserId reporterId, final List<PositionFix> positions) {
        requireDriver(reporterId);
        if (!TrackingSessionStatus.ACTIVE.equals(this.status)) {
            throw new FleetTrackingException(FleetTrackingException.Reason.CONFLICT, "Tracking session is finished");
        }
        positions.stream()
                .sorted(Comparator.comparingLong(PositionFix::sequence))
                .forEach(this::accept);
    }

    public void finish(final UserId driverId, final Instant finishedAt) {
        requireDriver(driverId);
        if (TrackingSessionStatus.FINISHED.equals(this.status)) {
            return;
        }
        this.status = TrackingSessionStatus.FINISHED;
        this.finishedAt = finishedAt;
    }

    private void accept(final PositionFix position) {
        if (position.accuracyMeters() != null && position.accuracyMeters() > MAX_ACCURACY_METERS) {
            return;
        }
        if (this.lastPosition != null && !followsLastPosition(position)) {
            return;
        }
        this.lastPosition = position;
        this.acceptedPositions.add(position);
    }

    private boolean followsLastPosition(final PositionFix position) {
        if (position.sequence() <= this.lastPosition.sequence()) {
            return false;
        }
        final Duration elapsed = position.timeSince(this.lastPosition);
        if (elapsed.isNegative() || elapsed.isZero()) {
            return false;
        }
        final double speed = this.lastPosition.distanceMetersTo(position) / (elapsed.toMillis() / 1000.0);
        return speed <= MAX_SPEED_METERS_PER_SECOND;
    }

    private void requireDriver(final UserId userId) {
        if (!this.driver.userId().equals(userId)) {
            throw new FleetTrackingException(FleetTrackingException.Reason.ACCESS_DENIED,
                    "Tracking session belongs to another driver");
        }
    }

    public TrackingSessionId trackingSessionId() {
        return trackingSessionId;
    }

    public TrackingDriver driver() {
        return driver;
    }

    public DeviceId deviceId() {
        return deviceId;
    }

    public VehicleId vehicleId() {
        return vehicleId;
    }

    public DepartmentCode departmentCode() {
        return departmentCode;
    }

    public Instant startedAt() {
        return startedAt;
    }

    public TrackingSessionStatus status() {
        return status;
    }

    public PositionFix lastPosition() {
        return lastPosition;
    }

    public Instant finishedAt() {
        return finishedAt;
    }

    public List<PositionFix> acceptedPositions() {
        return List.copyOf(acceptedPositions);
    }
}
