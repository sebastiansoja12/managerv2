package com.warehouse.auditlog.application.port.primary.command;

import com.warehouse.auditlog.domain.enumeration.AuditLogResourceType;
import com.warehouse.auditlog.domain.vo.AuditLogResourceId;
import com.warehouse.commonassets.identificator.UserId;

public record FindAuditLogEntriesCommand(AuditLogResourceType resourceType,
                                         AuditLogResourceId resourceId,
                                         UserId userId,
                                         int page,
                                         int size) {
}
