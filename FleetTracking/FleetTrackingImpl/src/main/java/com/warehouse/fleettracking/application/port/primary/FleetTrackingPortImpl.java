package com.warehouse.fleettracking.application.port.primary;

import com.warehouse.commonassets.identificator.TrackingSessionId;
import com.warehouse.fleettracking.application.port.primary.command.ReportPositionsCommand;
import com.warehouse.fleettracking.application.port.primary.command.StartTrackingSessionCommand;
import com.warehouse.fleettracking.application.port.secondary.TrackingDriverServicePort;
import com.warehouse.fleettracking.application.port.secondary.TrackingSessionRepository;
import com.warehouse.fleettracking.domain.exception.FleetTrackingException;
import com.warehouse.fleettracking.domain.model.TrackingSession;
import com.warehouse.fleettracking.domain.vo.PositionFix;
import com.warehouse.fleettracking.domain.vo.TrackingDriver;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

public class FleetTrackingPortImpl implements FleetTrackingPort {

    private final TrackingSessionRepository trackingSessionRepository;

    private final TrackingDriverServicePort trackingDriverServicePort;

    private final Clock clock;

    public FleetTrackingPortImpl(final TrackingSessionRepository trackingSessionRepository,
                                 final TrackingDriverServicePort trackingDriverServicePort,
                                 final Clock clock) {
        this.trackingSessionRepository = trackingSessionRepository;
        this.trackingDriverServicePort = trackingDriverServicePort;
        this.clock = clock;
    }

    @Override
    @Transactional
    public TrackingSessionId start(final StartTrackingSessionCommand command) {
        final TrackingDriver driver = currentDriver();
        this.trackingSessionRepository.findActiveByDriver(driver.userId()).forEach(previous -> {
            previous.finish(driver.userId(), this.clock.instant());
            this.trackingSessionRepository.update(previous);
        });
        final TrackingSession session = TrackingSession.start(driver, command.deviceId(), command.vehicleId(),
                this.trackingDriverServicePort.currentDepartmentCode().orElse(null), this.clock.instant());
        this.trackingSessionRepository.create(session);
        return session.trackingSessionId();
    }

    @Override
    @Transactional
    public void report(final ReportPositionsCommand command) {
        final TrackingDriver driver = currentDriver();
        final TrackingSession session = get(command.trackingSessionId());
        session.report(driver.userId(), command.positions());
        this.trackingSessionRepository.update(session);
    }

    @Override
    @Transactional
    public void finish(final TrackingSessionId trackingSessionId) {
        final TrackingDriver driver = currentDriver();
        final TrackingSession session = get(trackingSessionId);
        session.finish(driver.userId(), this.clock.instant());
        this.trackingSessionRepository.update(session);
    }

    @Override
    public List<TrackingSession> activeSessions() {
        return this.trackingSessionRepository.findActive();
    }

    @Override
    public List<PositionFix> route(final TrackingSessionId trackingSessionId) {
        return this.trackingSessionRepository.findPositions(get(trackingSessionId).trackingSessionId());
    }

    private TrackingSession get(final TrackingSessionId trackingSessionId) {
        return this.trackingSessionRepository.findById(trackingSessionId)
                .orElseThrow(() -> new FleetTrackingException(FleetTrackingException.Reason.NOT_FOUND,
                        "Tracking session not found"));
    }

    private TrackingDriver currentDriver() {
        return this.trackingDriverServicePort.currentDriver()
                .orElseThrow(() -> new FleetTrackingException(FleetTrackingException.Reason.ACCESS_DENIED,
                        "Fleet tracking requires a signed-in user"));
    }
}
