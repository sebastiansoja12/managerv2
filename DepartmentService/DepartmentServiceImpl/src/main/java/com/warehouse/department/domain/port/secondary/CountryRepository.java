package com.warehouse.department.domain.port.secondary;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.enumeration.CountryCode;

public interface CountryRepository {
    Country getCountryNameByCode(final CountryCode countryCode);
}
