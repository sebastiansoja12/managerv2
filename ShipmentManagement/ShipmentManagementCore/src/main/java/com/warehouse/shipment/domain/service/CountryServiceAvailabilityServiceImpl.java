package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.enumeration.Country;

public class CountryServiceAvailabilityServiceImpl implements CountryServiceAvailabilityService {


    @Override
    public boolean isCountryAvailable(final Country country) {
        return true;
    }
}
