package com.warehouse.fleettracking.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.TrackingPositionId;
import com.warehouse.fleettracking.domain.model.TrackingSession;
import com.warehouse.fleettracking.domain.vo.PositionFix;
import com.warehouse.fleettracking.domain.vo.TrackingDriver;
import com.warehouse.fleettracking.infrastructure.adapter.secondary.entity.PositionEmbeddable;
import com.warehouse.fleettracking.infrastructure.adapter.secondary.entity.TrackingPositionEntity;
import com.warehouse.fleettracking.infrastructure.adapter.secondary.entity.TrackingSessionEntity;

public class TrackingSessionPersistenceMapper {

    public TrackingSessionEntity toEntity(final TrackingSession trackingSession) {
        return new TrackingSessionEntity(
                trackingSession.trackingSessionId(),
                trackingSession.driver().userId(),
                trackingSession.driver().username(),
                trackingSession.deviceId(),
                trackingSession.vehicleId(),
                trackingSession.departmentCode(),
                trackingSession.status(),
                trackingSession.startedAt(),
                trackingSession.finishedAt(),
                trackingSession.lastPosition() == null ? null : toEmbeddable(trackingSession.lastPosition()));
    }

    public TrackingPositionEntity toEntity(final TrackingSession trackingSession, final PositionFix position) {
        return new TrackingPositionEntity(TrackingPositionId.nextId(), trackingSession.trackingSessionId(),
                toEmbeddable(position));
    }

    public TrackingSession toDomain(final TrackingSessionEntity entity) {
        return TrackingSession.rehydrate(
                entity.getTrackingSessionId(),
                new TrackingDriver(entity.getDriverUserId(), entity.getDriverUsername()),
                entity.getDeviceId(),
                entity.getVehicleId(),
                entity.getDepartmentCode(),
                entity.getStartedAt(),
                entity.getStatus(),
                entity.getLastPosition() == null || entity.getLastPosition().getSequence() == null ? null
                        : toDomain(entity.getLastPosition()),
                entity.getFinishedAt());
    }

    public PositionFix toDomain(final PositionEmbeddable position) {
        return new PositionFix(position.getSequence(), position.getLatitude(), position.getLongitude(),
                position.getAccuracyMeters(), position.getSpeedMetersPerSecond(), position.getHeadingDegrees(),
                position.getRecordedAt());
    }

    private PositionEmbeddable toEmbeddable(final PositionFix position) {
        return new PositionEmbeddable(position.sequence(), position.latitude(), position.longitude(),
                position.accuracyMeters(), position.speedMetersPerSecond(), position.headingDegrees(),
                position.recordedAt());
    }
}
