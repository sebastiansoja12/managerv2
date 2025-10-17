package com.warehouse.department.infrastructure.adapter.primary.api.dto;

import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

public record DepartmentCreateApiRequest(
        List<DepartmentCreateApi> departments
) {
    @JsonIgnore
    public String getResourceName() {
        return this.getClass().getSimpleName();
    }
}