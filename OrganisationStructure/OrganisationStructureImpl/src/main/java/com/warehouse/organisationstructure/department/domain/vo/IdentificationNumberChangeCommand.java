package com.warehouse.organisationstructure.department.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;

public record IdentificationNumberChangeCommand(DepartmentCode departmentCode, String identificationNumber) {
}
