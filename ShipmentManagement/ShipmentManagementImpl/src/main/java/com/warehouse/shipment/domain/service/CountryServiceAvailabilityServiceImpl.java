package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.shipment.domain.port.secondary.DepartmentRepository;

public class CountryServiceAvailabilityServiceImpl implements CountryServiceAvailabilityService {

    private final DepartmentRepository departmentRepository;

    public CountryServiceAvailabilityServiceImpl(final DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public boolean isCountryAvailable(final CountryCode countryCode) {
        return departmentRepository.existsAnyByCountryCode(countryCode);
    }
}
