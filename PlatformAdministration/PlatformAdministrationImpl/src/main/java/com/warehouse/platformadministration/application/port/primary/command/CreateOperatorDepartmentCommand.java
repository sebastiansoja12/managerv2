package com.warehouse.platformadministration.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentCode;

public record CreateOperatorDepartmentCommand(DepartmentCode departmentCode,
                                              String city,
                                              String street,
                                              String postalCode,
                                              String countryCode,
                                              String taxId,
                                              String telephoneNumber,
                                              String openingHours,
                                              String email,
                                              String departmentType) {
}
