package com.warehouse.auditlog.application.port.primary.result;

import com.warehouse.auditlog.domain.model.AuditLogEntry;

import java.util.List;
import java.util.Objects;

public record AuditLogEntryPage(List<AuditLogEntry> entries,
                                int page,
                                int size,
                                long totalElements) {

    public AuditLogEntryPage {
        entries = List.copyOf(Objects.requireNonNull(entries, "AuditLog entries cannot be null"));
    }
}
