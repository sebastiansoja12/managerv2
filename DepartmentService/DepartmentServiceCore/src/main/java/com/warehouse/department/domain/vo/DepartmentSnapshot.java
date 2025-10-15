package com.warehouse.department.domain.vo;

import com.warehouse.commonassets.enumeration.CountryCode;

public record DepartmentSnapshot(
        DepartmentCode departmentCode,
        Address address,
        String nip,
        String telephoneNumber,
        String openingHours,
        Boolean active,
        CountryCode countryCode
) {
}
