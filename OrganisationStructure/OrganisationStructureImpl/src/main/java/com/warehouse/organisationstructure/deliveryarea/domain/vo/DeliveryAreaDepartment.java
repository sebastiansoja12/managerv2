package com.warehouse.organisationstructure.deliveryarea.domain.vo;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;

public record DeliveryAreaDepartment(DepartmentId departmentId,
                                     DepartmentCode departmentCode,
                                     boolean active,
                                     CountryCode countryCode,
                                     GeoPoint location) {
}
