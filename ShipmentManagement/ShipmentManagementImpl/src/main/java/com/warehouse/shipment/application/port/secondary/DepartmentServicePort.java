package com.warehouse.shipment.application.port.secondary;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;

public interface DepartmentServicePort {

    boolean exists(final DepartmentId departmentId);

	DepartmentCode getDepartmentCode(final DepartmentId departmentId);

	DepartmentId getDepartmentId(final DepartmentCode departmentCode);

	boolean existsByCountryCode(final CountryCode countryCode);
}
