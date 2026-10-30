package com.warehouse.auditlog.domain.vo;

import java.util.Objects;
import java.util.UUID;

public record AuditLogEventId(UUID value) {

    public AuditLogEventId {
        Objects.requireNonNull(value, "Audit log event ID value cannot be null");
    }
}
