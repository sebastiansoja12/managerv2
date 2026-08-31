package com.warehouse.organisationstructure.department.infrastructure.adapter.primary.api.dto;

import java.util.Map;

public record DepartmentCreateApiResponse(Map<DepartmentApi, Boolean> departments) {
}
