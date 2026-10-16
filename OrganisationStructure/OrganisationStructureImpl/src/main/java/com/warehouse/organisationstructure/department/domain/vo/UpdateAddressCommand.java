package com.warehouse.organisationstructure.department.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;

public record UpdateAddressCommand(DepartmentCode departmentCode, Address address) {

}
