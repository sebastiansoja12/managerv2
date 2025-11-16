package com.warehouse.department.infrastructure.adapter.primary.api.dto;

public record IdentificationNumberChangeApiResponse(DepartmentCodeApi departmentCode, String oldIdentificationNumber, String newIdentificationNumber) {
}
