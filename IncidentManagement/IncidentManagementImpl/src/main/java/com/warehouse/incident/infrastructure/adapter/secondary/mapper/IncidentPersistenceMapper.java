package com.warehouse.incident.infrastructure.adapter.secondary.mapper;

import com.warehouse.incident.domain.model.Incident;
import com.warehouse.incident.domain.vo.IncidentActor;
import com.warehouse.incident.domain.vo.IncidentEntry;
import com.warehouse.incident.domain.vo.IncidentShipment;
import com.warehouse.incident.infrastructure.adapter.secondary.entity.IncidentEntity;
import com.warehouse.incident.infrastructure.adapter.secondary.entity.IncidentEntryEmbeddable;

public class IncidentPersistenceMapper {

    public IncidentEntity toEntity(final Incident incident) {
        return new IncidentEntity(
                incident.incidentId(),
                incident.shipment().shipmentId(),
                incident.shipment().trackingNumber(),
                incident.departmentCode(),
                incident.category(),
                incident.severity(),
                incident.status(),
                incident.description(),
                incident.reporter().userId(),
                incident.reporter().username(),
                incident.assignee() == null ? null : incident.assignee().userId(),
                incident.assignee() == null ? null : incident.assignee().username(),
                incident.resolution(),
                incident.reportedAt(),
                incident.resolvedAt(),
                incident.updatedAt(),
                incident.entries().stream()
                        .map(entry -> new IncidentEntryEmbeddable(entry.type(), entry.actor().userId(),
                                entry.actor().username(), entry.text(), entry.occurredAt()))
                        .toList());
    }

    public Incident toDomain(final IncidentEntity entity) {
        return Incident.rehydrate(
                entity.getIncidentId(),
                new IncidentShipment(entity.getShipmentId(), entity.getTrackingNumber()),
                entity.getDepartmentCode(),
                entity.getCategory(),
                entity.getSeverity(),
                entity.getDescription(),
                new IncidentActor(entity.getReporterUserId(), entity.getReporterUsername()),
                entity.getReportedAt(),
                entity.getEntries().stream()
                        .map(entry -> new IncidentEntry(entry.getType(),
                                new IncidentActor(entry.getUserId(), entry.getUsername()), entry.getText(),
                                entry.getOccurredAt()))
                        .toList(),
                entity.getStatus(),
                entity.getAssigneeUserId() == null ? null
                        : new IncidentActor(entity.getAssigneeUserId(), entity.getAssigneeUsername()),
                entity.getResolution(),
                entity.getResolvedAt(),
                entity.getUpdatedAt());
    }
}
