package com.warehouse.department.domain.vo;

public record IdentificationNumberChangeResponse(DepartmentCode departmentCode, String oldIdentificationNumber, String newIdentificationNumber) {
}
