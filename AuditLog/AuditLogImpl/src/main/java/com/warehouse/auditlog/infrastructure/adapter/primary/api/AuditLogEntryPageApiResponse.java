package com.warehouse.auditlog.infrastructure.adapter.primary.api;

import java.util.List;

public record AuditLogEntryPageApiResponse(List<AuditLogEntryApiResponse> content,
                                           int page,
                                           int size,
                                           long totalElements) {
}
