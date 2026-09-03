package com.warehouse.reroute.infrastructure.adapter.primary.validator;

import java.util.Arrays;
import java.util.Locale;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.reroute.infrastructure.adapter.primary.api.ReroutePartyApi;
import com.warehouse.reroute.infrastructure.adapter.primary.api.RerouteShipmentApiRequest;

public class RerouteRequestValidator {

    public void validate(final RerouteShipmentApiRequest request) {
        validateCountryCode(request.sender());
        validateCountryCode(request.recipient());
    }

    private void validateCountryCode(final ReroutePartyApi party) {
        final String countryCode = party.countryCode().trim().toUpperCase(Locale.ROOT);
        final boolean supported = Arrays.stream(CountryCode.values())
                .anyMatch(code -> code.name().equals(countryCode));
        if (!supported) {
            throw new RestException(400, "Unsupported country code: " + countryCode);
        }
    }
}
