package com.warehouse.department.domain.vo;

public record IdentificationNumberChangeResponse(DepartmentCode departmentCode, TaxId oldIdentificationNumber, TaxId newIdentificationNumber) {
}
