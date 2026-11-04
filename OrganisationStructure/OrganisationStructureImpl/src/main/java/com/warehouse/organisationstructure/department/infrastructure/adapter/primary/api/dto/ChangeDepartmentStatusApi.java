package com.warehouse.organisationstructure.department.infrastructure.adapter.primary.api.dto;

public record ChangeDepartmentStatusApi(DepartmentCodeApi departmentCode, String status, String reason) {

    public ChangeDepartmentStatusApi(final DepartmentCodeApi departmentCode, final String status) {
        this(departmentCode, status, null);
    }

    public String getResourceName() {
        return this.getClass().getSimpleName();
    }
}
