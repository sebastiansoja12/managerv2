package com.warehouse.auditlog.infrastructure.adapter.primary.mapper;

import com.warehouse.auditlog.api.event.AuditLogEntryIntegrationEvent;
import com.warehouse.auditlog.api.event.snapshot.AuditLogEntryData;
import com.warehouse.auditlog.application.port.primary.command.RecordAuditLogCommand;
import com.warehouse.auditlog.domain.enumeration.AuditLogResourceType;
import com.warehouse.auditlog.domain.vo.AuditLogChange;
import com.warehouse.auditlog.domain.vo.AuditLogEventId;
import com.warehouse.auditlog.domain.vo.AuditLogResource;
import com.warehouse.auditlog.domain.vo.AuditLogResourceId;

public class AuditLogEventMapper {

    public RecordAuditLogCommand toCommand(final AuditLogEntryIntegrationEvent event) {
        final AuditLogEntryData payload = event.payload();
        return new RecordAuditLogCommand(
                event.eventId().map(AuditLogEventId::new).orElse(null),
                new AuditLogResource(AuditLogResourceType.valueOf(payload.resourceType().name()),
                        new AuditLogResourceId(payload.resourceId().value())),
                payload.action(),
                payload.changes().stream()
                        .map(change -> new AuditLogChange(change.field(), change.previousValue(),
                                change.currentValue()))
                        .toList(),
                payload.occurredAt());
    }
}
