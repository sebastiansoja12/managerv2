package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.enumeration.CountryCode;

public interface CountryServiceAvailabilityService {
    boolean isCountryAvailable(final CountryCode code);
}
