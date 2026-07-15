package com.warehouse.shipment.application.service;

import com.warehouse.commonassets.enumeration.CountryCode;

public interface CountryServiceAvailabilityService {
    boolean isCountryAvailable(final CountryCode code);
}
