package com.warehouse.auditlog.infrastructure.adapter.primary.api;

public record AuditLogChangeApiResponse(String field, String previousValue, String currentValue) {
}
