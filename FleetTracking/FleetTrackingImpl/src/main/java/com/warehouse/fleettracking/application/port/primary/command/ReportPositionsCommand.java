package com.warehouse.fleettracking.application.port.primary.command;

import com.warehouse.commonassets.identificator.TrackingSessionId;
import com.warehouse.fleettracking.domain.vo.PositionFix;

import java.util.List;

public record ReportPositionsCommand(TrackingSessionId trackingSessionId, List<PositionFix> positions) {
}
