package com.warehouse.auditlog.api.event.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.auditlog.api.dto.AuditLogChangeDto;
import com.warehouse.auditlog.api.dto.AuditLogResourceIdDto;
import com.warehouse.auditlog.api.dto.AuditLogResourceTypeDto;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuditLogEntryData(AuditLogResourceTypeDto resourceType,
                                AuditLogResourceIdDto resourceId,
                                String action,
                                List<AuditLogChangeDto> changes,
                                Instant occurredAt) {

    public AuditLogEntryData {
        Objects.requireNonNull(resourceType, "Audit log resource type cannot be null");
        Objects.requireNonNull(resourceId, "Audit log resource ID cannot be null");
        Objects.requireNonNull(action, "Audit log action cannot be null");
        Objects.requireNonNull(occurredAt, "Audit log occurrence time cannot be null");
        changes = List.copyOf(Objects.requireNonNull(changes, "Audit log changes cannot be null"));
    }
}
