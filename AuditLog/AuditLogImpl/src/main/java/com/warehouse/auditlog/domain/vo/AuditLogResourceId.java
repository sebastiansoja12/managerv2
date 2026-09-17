package com.warehouse.auditlog.domain.vo;

import java.util.Objects;

public record AuditLogResourceId(String value) {

    public AuditLogResourceId {
        Objects.requireNonNull(value, "AuditLog resource ID value cannot be null");
    }
}
