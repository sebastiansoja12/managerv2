package com.warehouse.organisationstructure.department.domain.vo;

import com.warehouse.commonassets.enumeration.CountryCode;

public record DepartmentAddress(
        String city,
        String postalCode,
        String street,
        CountryCode countryCode
) {
}
