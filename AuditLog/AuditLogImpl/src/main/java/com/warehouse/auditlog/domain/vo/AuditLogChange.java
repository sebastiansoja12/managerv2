package com.warehouse.auditlog.domain.vo;

import java.util.Objects;

public record AuditLogChange(String field, String previousValue, String currentValue) {

    private static final int MAX_VALUE_LENGTH = 1000;

    public AuditLogChange {
        Objects.requireNonNull(field, "AuditLog change field cannot be null");
        previousValue = truncate(previousValue);
        currentValue = truncate(currentValue);
    }

    private static String truncate(final String value) {
        return value != null && value.length() > MAX_VALUE_LENGTH ? value.substring(0, MAX_VALUE_LENGTH) : value;
    }
}
