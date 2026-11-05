package com.warehouse.auditlog.domain.model;

import com.warehouse.auditlog.domain.vo.AuditLogActor;
import com.warehouse.auditlog.domain.vo.AuditLogChange;
import com.warehouse.auditlog.domain.vo.AuditLogEntryId;
import com.warehouse.auditlog.domain.vo.AuditLogEventId;
import com.warehouse.auditlog.domain.vo.AuditLogResource;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public record AuditLogEntry(AuditLogEntryId auditLogEntryId,
                            AuditLogEventId eventId,
                            AuditLogResource resource,
                            String action,
                            List<AuditLogChange> changes,
                            AuditLogActor actor,
                            Instant occurredAt) {

    public AuditLogEntry {
        Objects.requireNonNull(resource, "Audit log resource cannot be null");
        Objects.requireNonNull(action, "Audit log action cannot be null");
        Objects.requireNonNull(actor, "Audit log actor cannot be null");
        Objects.requireNonNull(occurredAt, "Audit log occurrence time cannot be null");
        changes = List.copyOf(Objects.requireNonNull(changes, "Audit log changes cannot be null"));
    }

    public static AuditLogEntry record(final AuditLogEventId eventId,
                                       final AuditLogResource resource,
                                       final String action,
                                       final List<AuditLogChange> changes,
                                       final AuditLogActor actor,
                                       final Instant occurredAt) {
        return new AuditLogEntry(null, eventId, resource, action, changes, actor, occurredAt);
    }
}
