package com.warehouse.department.api.event;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public record OperatorInitialDepartmentCreateEvent(OperatorId operatorId,
                                                   UserId adminUserId,
                                                   DepartmentCode departmentCode,
                                                   String companyName,
                                                   String taxId,
                                                   String contactPhone,
                                                   String contactEmail,
                                                   String city,
                                                   String street,
                                                   String postalCode,
                                                   String countryCode,
                                                   String openingHours,
                                                   String departmentType) {
}
