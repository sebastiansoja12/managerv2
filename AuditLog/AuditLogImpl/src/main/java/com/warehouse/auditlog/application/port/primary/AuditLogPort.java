package com.warehouse.auditlog.application.port.primary;

import com.warehouse.auditlog.application.port.primary.command.FindAuditLogEntriesCommand;
import com.warehouse.auditlog.application.port.primary.command.RecordAuditLogCommand;
import com.warehouse.auditlog.application.port.primary.result.AuditLogEntryPage;

public interface AuditLogPort {

    void record(final RecordAuditLogCommand command);

    AuditLogEntryPage find(final FindAuditLogEntriesCommand command);
}
