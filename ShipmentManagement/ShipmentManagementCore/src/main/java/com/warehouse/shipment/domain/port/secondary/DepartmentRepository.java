package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.commonassets.enumeration.CountryCode;

public interface DepartmentRepository {
    boolean existsAnyByCountryCode(final CountryCode countryCode);
}
