package com.warehouse.shipment.application.service;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.shipment.application.port.secondary.DepartmentServicePort;

public class DepartmentCountryAvailabilityServiceImpl implements DepartmentCountryAvailabilityService {

    private final DepartmentServicePort departmentServicePort;

    public DepartmentCountryAvailabilityServiceImpl(final DepartmentServicePort departmentServicePort) {
        this.departmentServicePort = departmentServicePort;
    }

    @Override
    public boolean isCountryAvailable(final CountryCode countryCode) {
        return departmentServicePort.existsByCountryCode(countryCode);
    }
}
