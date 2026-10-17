package com.warehouse.platformadministration.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;

public record CreateOperatorCourierCommand(SupplierCode supplierCode,
                                           String firstName,
                                           String lastName,
                                           String telephoneNumber,
                                           DepartmentCode departmentCode) {
}
