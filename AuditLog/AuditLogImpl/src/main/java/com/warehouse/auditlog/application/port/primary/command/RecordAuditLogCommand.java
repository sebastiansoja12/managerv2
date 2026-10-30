package com.warehouse.auditlog.application.port.primary.command;

import com.warehouse.auditlog.domain.vo.AuditLogChange;
import com.warehouse.auditlog.domain.vo.AuditLogEventId;
import com.warehouse.auditlog.domain.vo.AuditLogResource;

import java.time.Instant;
import java.util.List;

public record RecordAuditLogCommand(AuditLogEventId eventId,
                                    AuditLogResource resource,
                                    String action,
                                    List<AuditLogChange> changes,
                                    Instant occurredAt) {
}
