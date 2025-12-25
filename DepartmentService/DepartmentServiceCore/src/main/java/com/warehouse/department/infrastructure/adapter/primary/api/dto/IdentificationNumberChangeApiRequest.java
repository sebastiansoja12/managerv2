package com.warehouse.department.infrastructure.adapter.primary.api.dto;

public record IdentificationNumberChangeApiRequest(DepartmentCodeApi departmentCode, String identificationNumber) {
    public String getResourceName() {
        return this.getClass().getSimpleName();
    }
}
