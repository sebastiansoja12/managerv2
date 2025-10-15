package com.warehouse.department.infrastructure.adapter.primary.api.dto;

import java.util.List;

public record DepartmentCreateApiRequest(
        List<DepartmentCreateApi> departments
) {
    public String getResourceName() {
        return this.getClass().getSimpleName();
    }
}