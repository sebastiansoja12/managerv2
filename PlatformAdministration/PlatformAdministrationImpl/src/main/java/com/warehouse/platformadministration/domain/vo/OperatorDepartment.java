package com.warehouse.platformadministration.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;

public record OperatorDepartment(DepartmentId departmentId,
                                 DepartmentCode departmentCode,
                                 String city,
                                 String street,
                                 String postalCode,
                                 String countryCode,
                                 String taxId,
                                 String telephoneNumber,
                                 String openingHours,
                                 String email,
                                 String departmentType,
                                 String status) {
}
