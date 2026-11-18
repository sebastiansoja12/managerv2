package com.warehouse.shipment.application.port.secondary;

import java.util.Optional;

import com.warehouse.commonassets.enumeration.CountryCode;

public interface PostalCodeServicePort {

    Optional<CountryCode> findCountryCode(final String postalCode, final String city);
}
