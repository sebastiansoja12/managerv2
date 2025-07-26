package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.enumeration.Country;

public interface CountryServiceAvailabilityService {
    boolean isCountryAvailable(final Country country);
}
