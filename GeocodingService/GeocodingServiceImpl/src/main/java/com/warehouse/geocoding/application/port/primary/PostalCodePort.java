package com.warehouse.geocoding.application.port.primary;

import java.util.List;
import java.util.Optional;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.geocoding.domain.vo.PostalCode;

public interface PostalCodePort {

    List<PostalCode> search(final String query);

    Optional<CountryCode> findCountryCode(final String postalCode, final String city);
}
