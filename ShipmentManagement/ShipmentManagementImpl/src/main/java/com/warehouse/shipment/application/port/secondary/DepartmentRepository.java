package com.warehouse.shipment.application.port.secondary;

import com.warehouse.commonassets.enumeration.CountryCode;

public interface DepartmentRepository {
    boolean existsAnyByCountryCode(final CountryCode countryCode);
}
