package com.warehouse.fleettracking.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.TrackingSessionId;
import com.warehouse.fleettracking.application.port.primary.FleetTrackingPort;
import com.warehouse.fleettracking.infrastructure.adapter.primary.api.PositionFixApiResponse;
import com.warehouse.fleettracking.infrastructure.adapter.primary.api.ReportPositionsApiRequest;
import com.warehouse.fleettracking.infrastructure.adapter.primary.api.StartTrackingSessionApiRequest;
import com.warehouse.fleettracking.infrastructure.adapter.primary.api.TrackingSessionApiResponse;
import com.warehouse.fleettracking.infrastructure.adapter.primary.api.TrackingSessionIdDto;
import com.warehouse.fleettracking.infrastructure.adapter.primary.mapper.FleetTrackingWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fleet-tracking/sessions")
public class FleetTrackingController {

    private final FleetTrackingPort fleetTrackingPort;

    private final FleetTrackingWebMapper fleetTrackingWebMapper;

    public FleetTrackingController(final FleetTrackingPort fleetTrackingPort,
                                   final FleetTrackingWebMapper fleetTrackingWebMapper) {
        this.fleetTrackingPort = fleetTrackingPort;
        this.fleetTrackingWebMapper = fleetTrackingWebMapper;
    }

    @PostMapping
    public ResponseEntity<TrackingSessionIdDto> start(@Valid @RequestBody final StartTrackingSessionApiRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.fleetTrackingWebMapper.toResponse(this.fleetTrackingPort.start(
                        this.fleetTrackingWebMapper.toCommand(request))));
    }

    @PostMapping("/{trackingSessionId}/positions")
    public ResponseEntity<Void> report(@PathVariable final TrackingSessionId trackingSessionId,
                                       @Valid @RequestBody final ReportPositionsApiRequest request) {
        this.fleetTrackingPort.report(this.fleetTrackingWebMapper.toCommand(trackingSessionId, request));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{trackingSessionId}/finish")
    public ResponseEntity<Void> finish(@PathVariable final TrackingSessionId trackingSessionId) {
        this.fleetTrackingPort.finish(trackingSessionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<TrackingSessionApiResponse>> active() {
        return ResponseEntity.ok(this.fleetTrackingWebMapper.toSessionResponses(
                this.fleetTrackingPort.activeSessions()));
    }

    @GetMapping("/{trackingSessionId}/positions")
    public ResponseEntity<List<PositionFixApiResponse>> route(@PathVariable final TrackingSessionId trackingSessionId) {
        return ResponseEntity.ok(this.fleetTrackingWebMapper.toPositionResponses(
                this.fleetTrackingPort.route(trackingSessionId)));
    }
}
