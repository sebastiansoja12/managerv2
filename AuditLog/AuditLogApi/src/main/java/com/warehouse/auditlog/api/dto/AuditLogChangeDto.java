package com.warehouse.auditlog.api.dto;

public record AuditLogChangeDto(String field, String previousValue, String currentValue) {
}
