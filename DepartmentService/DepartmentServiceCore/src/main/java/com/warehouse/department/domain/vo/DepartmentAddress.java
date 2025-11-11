package com.warehouse.department.domain.vo;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.enumeration.CountryCode;

public record DepartmentAddress(
        String city,
        Country country,
        String postalCode,
        String street,
        CountryCode countryCode
) {
}

