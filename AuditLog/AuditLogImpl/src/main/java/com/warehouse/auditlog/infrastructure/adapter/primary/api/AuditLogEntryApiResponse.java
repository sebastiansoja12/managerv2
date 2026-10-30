package com.warehouse.auditlog.infrastructure.adapter.primary.api;

import com.warehouse.auditlog.domain.enumeration.AuditLogResourceType;

import java.time.Instant;
import java.util.List;

public record AuditLogEntryApiResponse(Long id,
                                       AuditLogResourceType resourceType,
                                       String resourceId,
                                       String action,
                                       List<AuditLogChangeApiResponse> changes,
                                       Long userId,
                                       String username,
                                       boolean system,
                                       Long departmentId,
                                       Instant occurredAt) {
}
