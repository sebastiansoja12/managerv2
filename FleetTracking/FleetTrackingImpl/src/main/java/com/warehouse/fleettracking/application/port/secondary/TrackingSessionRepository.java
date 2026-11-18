package com.warehouse.fleettracking.application.port.secondary;

import com.warehouse.commonassets.identificator.TrackingSessionId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.fleettracking.domain.model.TrackingSession;
import com.warehouse.fleettracking.domain.vo.PositionFix;

import java.util.List;
import java.util.Optional;

public interface TrackingSessionRepository {

    void create(final TrackingSession trackingSession);

    void update(final TrackingSession trackingSession);

    Optional<TrackingSession> findById(final TrackingSessionId trackingSessionId);

    List<TrackingSession> findActiveByDriver(final UserId driverId);

    List<TrackingSession> findActive();

    List<PositionFix> findPositions(final TrackingSessionId trackingSessionId);
}
