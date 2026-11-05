package com.warehouse.geocoding.infrastructure.adapter.primary;

import java.util.Optional;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.geocoding.api.PostalCodeService;
import com.warehouse.geocoding.application.port.primary.PostalCodePort;

public class PostalCodeServiceAdapter implements PostalCodeService {

    private final PostalCodePort postalCodePort;

    public PostalCodeServiceAdapter(final PostalCodePort postalCodePort) {
        this.postalCodePort = postalCodePort;
    }

    @Override
    public Optional<CountryCode> findCountryCode(final String postalCode, final String city) {
        return postalCodePort.findCountryCode(postalCode, city);
    }
}
