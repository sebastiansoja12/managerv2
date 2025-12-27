package com.warehouse.department.infrastructure.adapter.primary.api.dto;

public record ChangeDepartmentStatusApi(DepartmentCodeApi departmentCode, String status) {
    public String getResourceName() {
        return this.getClass().getSimpleName();
    }
}
