package com.warehouse.geocoding.infrastructure.adapter.primary.api;

import com.warehouse.geocoding.domain.vo.PostalCode;

public record PostalCodeApiResponse(String countryCode, String postalCode, String city, String regionCode) {

    public static PostalCodeApiResponse from(final PostalCode postalCode) {
        return new PostalCodeApiResponse(postalCode.countryCode().name(), postalCode.postalCode(),
                postalCode.city(), postalCode.regionCode());
    }
}
