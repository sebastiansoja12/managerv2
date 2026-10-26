package com.warehouse.geocoding.api;

import java.util.Optional;

import com.warehouse.commonassets.enumeration.CountryCode;

public interface PostalCodeService {

    Optional<CountryCode> findCountryCode(final String postalCode, final String city);
}
