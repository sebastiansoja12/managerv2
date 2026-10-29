package com.warehouse.incident.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.IncidentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.incident.application.port.primary.IncidentPort;
import com.warehouse.incident.domain.enumeration.IncidentCategory;
import com.warehouse.incident.domain.enumeration.IncidentSeverity;
import com.warehouse.incident.domain.enumeration.IncidentStatus;
import com.warehouse.incident.infrastructure.adapter.primary.api.CommentIncidentApiRequest;
import com.warehouse.incident.infrastructure.adapter.primary.api.IncidentApiResponse;
import com.warehouse.incident.infrastructure.adapter.primary.api.IncidentIdDto;
import com.warehouse.incident.infrastructure.adapter.primary.api.IncidentPageApiResponse;
import com.warehouse.incident.infrastructure.adapter.primary.api.IncidentStatisticsApiResponse;
import com.warehouse.incident.infrastructure.adapter.primary.api.ReopenIncidentApiRequest;
import com.warehouse.incident.infrastructure.adapter.primary.api.ReportIncidentApiRequest;
import com.warehouse.incident.infrastructure.adapter.primary.api.ResolveIncidentApiRequest;
import com.warehouse.incident.infrastructure.adapter.primary.mapper.IncidentWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    private final IncidentPort incidentPort;

    private final IncidentWebMapper incidentWebMapper;

    public IncidentController(final IncidentPort incidentPort,
                              final IncidentWebMapper incidentWebMapper) {
        this.incidentPort = incidentPort;
        this.incidentWebMapper = incidentWebMapper;
    }

    @GetMapping
    public ResponseEntity<IncidentPageApiResponse> search(
            @RequestParam(required = false) final IncidentStatus status,
            @RequestParam(required = false) final IncidentSeverity severity,
            @RequestParam(required = false) final IncidentCategory category,
            @RequestParam(required = false) final ShipmentId shipmentId,
            @RequestParam(defaultValue = "false") final boolean assignedToMe,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "20") final int size) {
        return ResponseEntity.ok(this.incidentWebMapper.toResponse(this.incidentPort.find(
                this.incidentWebMapper.toFindCommand(status, severity, category, shipmentId, assignedToMe, page,
                        size))));
    }

    @GetMapping("/statistics")
    public ResponseEntity<IncidentStatisticsApiResponse> statistics() {
        return ResponseEntity.ok(this.incidentWebMapper.toResponse(this.incidentPort.statistics()));
    }

    @GetMapping("/{incidentId}")
    public ResponseEntity<IncidentApiResponse> get(@PathVariable final IncidentId incidentId) {
        return ResponseEntity.ok(this.incidentWebMapper.toResponse(this.incidentPort.get(incidentId)));
    }

    @PostMapping
    public ResponseEntity<IncidentIdDto> report(@Valid @RequestBody final ReportIncidentApiRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.incidentWebMapper.toResponse(this.incidentPort.report(
                        this.incidentWebMapper.toCommand(request))));
    }

    @PostMapping("/{incidentId}/assign")
    public ResponseEntity<Void> assignToCurrentUser(@PathVariable final IncidentId incidentId) {
        this.incidentPort.assignToCurrentUser(incidentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{incidentId}/comments")
    public ResponseEntity<Void> comment(@PathVariable final IncidentId incidentId,
                                        @Valid @RequestBody final CommentIncidentApiRequest request) {
        this.incidentPort.comment(this.incidentWebMapper.toCommand(incidentId, request));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{incidentId}/resolve")
    public ResponseEntity<Void> resolve(@PathVariable final IncidentId incidentId,
                                        @Valid @RequestBody final ResolveIncidentApiRequest request) {
        this.incidentPort.resolve(this.incidentWebMapper.toCommand(incidentId, request));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{incidentId}/reopen")
    public ResponseEntity<Void> reopen(@PathVariable final IncidentId incidentId,
                                       @Valid @RequestBody final ReopenIncidentApiRequest request) {
        this.incidentPort.reopen(this.incidentWebMapper.toCommand(incidentId, request));
        return ResponseEntity.noContent().build();
    }
}
