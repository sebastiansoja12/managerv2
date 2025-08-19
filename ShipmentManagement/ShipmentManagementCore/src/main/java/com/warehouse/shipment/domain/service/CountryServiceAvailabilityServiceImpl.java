package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.shipment.domain.port.secondary.DepartmentRepository;

public class CountryServiceAvailabilityServiceImpl implements CountryServiceAvailabilityService {

    private final DepartmentRepository departmentRepository;

    public CountryServiceAvailabilityServiceImpl(final DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public boolean isCountryAvailable(final Country country) {
        return departmentRepository.existsAnyByCountry(country);
    }
}
