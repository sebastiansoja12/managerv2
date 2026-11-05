package com.warehouse.auditlog.domain.vo;

import com.warehouse.auditlog.domain.enumeration.AuditLogResourceType;

import java.util.Objects;

public record AuditLogResource(AuditLogResourceType type, AuditLogResourceId resourceId) {

    public AuditLogResource {
        Objects.requireNonNull(type, "AuditLog resource type cannot be null");
        Objects.requireNonNull(resourceId, "AuditLog resource ID cannot be null");
    }
}
