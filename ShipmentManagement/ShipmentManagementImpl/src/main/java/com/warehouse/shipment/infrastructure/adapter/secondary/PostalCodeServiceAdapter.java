package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.Optional;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.geocoding.api.PostalCodeService;
import com.warehouse.shipment.application.port.secondary.PostalCodeServicePort;

public class PostalCodeServiceAdapter implements PostalCodeServicePort {

    private final PostalCodeService postalCodeService;

    public PostalCodeServiceAdapter(final PostalCodeService postalCodeService) {
        this.postalCodeService = postalCodeService;
    }

    @Override
    public Optional<CountryCode> findCountryCode(final String postalCode, final String city) {
        return postalCodeService.findCountryCode(postalCode, city);
    }
}
