package com.warehouse.mcp.domain.vo;

public record DepartmentFilter(String status, String type) {

    public boolean matches(final DepartmentEntry department) {
        return (status == null || status.isBlank() || status.equalsIgnoreCase(department.status()))
                && (type == null || type.isBlank() || type.equalsIgnoreCase(department.type()));
    }
}
