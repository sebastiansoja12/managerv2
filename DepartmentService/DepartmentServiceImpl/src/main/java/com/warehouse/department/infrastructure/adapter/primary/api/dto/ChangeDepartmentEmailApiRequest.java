package com.warehouse.department.infrastructure.adapter.primary.api.dto;

public record ChangeDepartmentEmailApiRequest(DepartmentCodeApi departmentCode, String email) {
    public String getResourceName() {
        return this.getClass().getSimpleName();
    }
}
