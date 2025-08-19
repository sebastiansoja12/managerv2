package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.commonassets.enumeration.Country;

public interface DepartmentRepository {
    boolean existsAnyByCountry(final Country country);
}
