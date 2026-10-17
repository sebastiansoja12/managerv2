package com.warehouse.organisationstructure.department.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;

public record IdentificationNumberChangeResponse(DepartmentCode departmentCode, TaxId oldIdentificationNumber, TaxId newIdentificationNumber) {
}
