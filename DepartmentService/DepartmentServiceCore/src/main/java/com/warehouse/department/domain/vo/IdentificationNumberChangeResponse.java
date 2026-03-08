package com.warehouse.department.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;

public record IdentificationNumberChangeResponse(DepartmentCode departmentCode, TaxId oldIdentificationNumber, TaxId newIdentificationNumber) {
}
