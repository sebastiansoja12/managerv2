package com.warehouse.auditlog.api.dto;

import java.util.Objects;

public record AuditLogResourceIdDto(String value) {

    public AuditLogResourceIdDto {
        Objects.requireNonNull(value, "AuditLog resource ID value cannot be null");
    }

    public static AuditLogResourceIdDto of(final Object value) {
        return new AuditLogResourceIdDto(String.valueOf(value));
    }
}
