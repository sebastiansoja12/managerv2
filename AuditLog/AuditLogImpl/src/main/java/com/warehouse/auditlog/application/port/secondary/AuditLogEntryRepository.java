package com.warehouse.auditlog.application.port.secondary;

import com.warehouse.auditlog.application.port.primary.command.FindAuditLogEntriesCommand;
import com.warehouse.auditlog.application.port.primary.result.AuditLogEntryPage;
import com.warehouse.auditlog.domain.model.AuditLogEntry;
import com.warehouse.auditlog.domain.vo.AuditLogEventId;

public interface AuditLogEntryRepository {

    void save(final AuditLogEntry auditLogEntry);

    boolean exists(final AuditLogEventId eventId);

    AuditLogEntryPage find(final FindAuditLogEntriesCommand command);
}
