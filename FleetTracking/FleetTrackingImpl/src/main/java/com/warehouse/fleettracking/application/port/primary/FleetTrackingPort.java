package com.warehouse.fleettracking.application.port.primary;

import com.warehouse.commonassets.identificator.TrackingSessionId;
import com.warehouse.fleettracking.application.port.primary.command.ReportPositionsCommand;
import com.warehouse.fleettracking.application.port.primary.command.StartTrackingSessionCommand;
import com.warehouse.fleettracking.domain.model.TrackingSession;
import com.warehouse.fleettracking.domain.vo.PositionFix;

import java.util.List;

public interface FleetTrackingPort {

    TrackingSessionId start(final StartTrackingSessionCommand command);

    void report(final ReportPositionsCommand command);

    void finish(final TrackingSessionId trackingSessionId);

    List<TrackingSession> activeSessions();

    List<PositionFix> route(final TrackingSessionId trackingSessionId);
}
