package com.warehouse.incident.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.IncidentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.incident.application.port.primary.command.CommentIncidentCommand;
import com.warehouse.incident.application.port.primary.command.FindIncidentsCommand;
import com.warehouse.incident.application.port.primary.command.ReopenIncidentCommand;
import com.warehouse.incident.application.port.primary.command.ReportIncidentCommand;
import com.warehouse.incident.application.port.primary.command.ResolveIncidentCommand;
import com.warehouse.incident.application.port.primary.result.IncidentPage;
import com.warehouse.incident.application.port.primary.result.IncidentStatistics;
import com.warehouse.incident.domain.enumeration.IncidentCategory;
import com.warehouse.incident.domain.enumeration.IncidentSeverity;
import com.warehouse.incident.domain.enumeration.IncidentStatus;
import com.warehouse.incident.domain.model.Incident;
import com.warehouse.incident.domain.vo.IncidentActor;
import com.warehouse.incident.infrastructure.adapter.primary.api.CommentIncidentApiRequest;
import com.warehouse.incident.infrastructure.adapter.primary.api.IncidentActorApiResponse;
import com.warehouse.incident.infrastructure.adapter.primary.api.IncidentApiResponse;
import com.warehouse.incident.infrastructure.adapter.primary.api.IncidentEntryApiResponse;
import com.warehouse.incident.infrastructure.adapter.primary.api.IncidentIdDto;
import com.warehouse.incident.infrastructure.adapter.primary.api.IncidentPageApiResponse;
import com.warehouse.incident.infrastructure.adapter.primary.api.IncidentStatisticsApiResponse;
import com.warehouse.incident.infrastructure.adapter.primary.api.ReopenIncidentApiRequest;
import com.warehouse.incident.infrastructure.adapter.primary.api.ReportIncidentApiRequest;
import com.warehouse.incident.infrastructure.adapter.primary.api.ResolveIncidentApiRequest;
import com.warehouse.incident.infrastructure.adapter.primary.api.ShipmentIdDto;
import com.warehouse.incident.infrastructure.adapter.primary.api.UserIdDto;

public class IncidentWebMapper {

    public ReportIncidentCommand toCommand(final ReportIncidentApiRequest request) {
        return new ReportIncidentCommand(new ShipmentId(request.shipmentId().value()), request.category(),
                request.severity(), request.description());
    }

    public CommentIncidentCommand toCommand(final IncidentId incidentId, final CommentIncidentApiRequest request) {
        return new CommentIncidentCommand(incidentId, request.text());
    }

    public ResolveIncidentCommand toCommand(final IncidentId incidentId, final ResolveIncidentApiRequest request) {
        return new ResolveIncidentCommand(incidentId, request.resolution(), request.note());
    }

    public ReopenIncidentCommand toCommand(final IncidentId incidentId, final ReopenIncidentApiRequest request) {
        return new ReopenIncidentCommand(incidentId, request.reason());
    }

    public FindIncidentsCommand toFindCommand(final IncidentStatus status,
                                              final IncidentSeverity severity,
                                              final IncidentCategory category,
                                              final ShipmentId shipmentId,
                                              final boolean assignedToMe,
                                              final int page,
                                              final int size) {
        return new FindIncidentsCommand(status, severity, category,
                shipmentId, assignedToMe, null, page, size);
    }

    public IncidentIdDto toResponse(final IncidentId incidentId) {
        return new IncidentIdDto(incidentId.value());
    }

    public IncidentPageApiResponse toResponse(final IncidentPage incidentPage) {
        return new IncidentPageApiResponse(
                incidentPage.incidents().stream().map(this::toResponse).toList(),
                incidentPage.page(),
                incidentPage.size(),
                incidentPage.totalElements());
    }

    public IncidentStatisticsApiResponse toResponse(final IncidentStatistics statistics) {
        return new IncidentStatisticsApiResponse(statistics.open(), statistics.critical(), statistics.overdue(),
                statistics.resolvedToday());
    }

    public IncidentApiResponse toResponse(final Incident incident) {
        return new IncidentApiResponse(
                new IncidentIdDto(incident.incidentId().value()),
                new ShipmentIdDto(incident.shipment().shipmentId().getValue()),
                incident.shipment().trackingNumber() == null ? null : incident.shipment().trackingNumber().value(),
                incident.departmentCode() == null ? null : incident.departmentCode().value(),
                incident.category(),
                incident.severity(),
                incident.status(),
                incident.description(),
                toResponse(incident.reporter()),
                incident.assignee() == null ? null : toResponse(incident.assignee()),
                incident.resolution(),
                incident.reportedAt(),
                incident.resolvedAt(),
                incident.updatedAt(),
                incident.entries().stream()
                        .map(entry -> new IncidentEntryApiResponse(entry.type(), toResponse(entry.actor()),
                                entry.text(), entry.occurredAt()))
                        .toList());
    }

    private IncidentActorApiResponse toResponse(final IncidentActor actor) {
        return new IncidentActorApiResponse(new UserIdDto(actor.userId().value()),
                actor.username() == null ? null : actor.username().value());
    }
}
