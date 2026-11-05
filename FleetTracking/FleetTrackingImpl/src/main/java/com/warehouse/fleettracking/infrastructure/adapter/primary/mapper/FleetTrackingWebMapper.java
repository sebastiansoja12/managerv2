package com.warehouse.fleettracking.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.TrackingSessionId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.fleettracking.application.port.primary.command.ReportPositionsCommand;
import com.warehouse.fleettracking.application.port.primary.command.StartTrackingSessionCommand;
import com.warehouse.fleettracking.domain.model.TrackingSession;
import com.warehouse.fleettracking.domain.vo.PositionFix;
import com.warehouse.fleettracking.infrastructure.adapter.primary.api.PositionFixApiRequest;
import com.warehouse.fleettracking.infrastructure.adapter.primary.api.PositionFixApiResponse;
import com.warehouse.fleettracking.infrastructure.adapter.primary.api.ReportPositionsApiRequest;
import com.warehouse.fleettracking.infrastructure.adapter.primary.api.StartTrackingSessionApiRequest;
import com.warehouse.fleettracking.infrastructure.adapter.primary.api.TrackingSessionApiResponse;
import com.warehouse.fleettracking.infrastructure.adapter.primary.api.TrackingSessionIdDto;
import com.warehouse.fleettracking.infrastructure.adapter.primary.api.UserIdDto;
import com.warehouse.fleettracking.infrastructure.adapter.primary.api.VehicleIdDto;

import java.util.List;

public class FleetTrackingWebMapper {

    public StartTrackingSessionCommand toCommand(final StartTrackingSessionApiRequest request) {
        return new StartTrackingSessionCommand(new DeviceId(request.deviceId().value()),
                request.vehicleId() == null || request.vehicleId().value() == null ? null
                        : new VehicleId(request.vehicleId().value()));
    }

    public ReportPositionsCommand toCommand(final TrackingSessionId trackingSessionId,
                                            final ReportPositionsApiRequest request) {
        return new ReportPositionsCommand(trackingSessionId, request.positions().stream()
                .map(this::toDomain)
                .toList());
    }

    public TrackingSessionIdDto toResponse(final TrackingSessionId trackingSessionId) {
        return new TrackingSessionIdDto(trackingSessionId.value());
    }

    public List<TrackingSessionApiResponse> toSessionResponses(final List<TrackingSession> trackingSessions) {
        return trackingSessions.stream().map(this::toResponse).toList();
    }

    public List<PositionFixApiResponse> toPositionResponses(final List<PositionFix> positions) {
        return positions.stream().map(this::toResponse).toList();
    }

    private TrackingSessionApiResponse toResponse(final TrackingSession trackingSession) {
        return new TrackingSessionApiResponse(
                toResponse(trackingSession.trackingSessionId()),
                new UserIdDto(trackingSession.driver().userId().value()),
                trackingSession.driver().username() == null ? null : trackingSession.driver().username().value(),
                trackingSession.deviceId().value(),
                trackingSession.vehicleId() == null ? null : new VehicleIdDto(trackingSession.vehicleId().value()),
                trackingSession.departmentCode() == null ? null : trackingSession.departmentCode().value(),
                trackingSession.status(),
                trackingSession.startedAt(),
                trackingSession.finishedAt(),
                trackingSession.lastPosition() == null ? null : toResponse(trackingSession.lastPosition()));
    }

    private PositionFix toDomain(final PositionFixApiRequest request) {
        return new PositionFix(request.sequence(), request.latitude(), request.longitude(), request.accuracyMeters(),
                request.speedMetersPerSecond(), request.headingDegrees(), request.recordedAt());
    }

    private PositionFixApiResponse toResponse(final PositionFix position) {
        return new PositionFixApiResponse(position.sequence(), position.latitude(), position.longitude(),
                position.accuracyMeters(), position.speedMetersPerSecond(), position.headingDegrees(),
                position.recordedAt());
    }
}
