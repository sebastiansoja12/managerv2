package com.warehouse.department.infrastructure.adapter.primary.api.dto;

import java.util.Map;

public record DepartmentCreateApiResponse(Map<DepartmentCodeApi, Boolean> departments) {
}
