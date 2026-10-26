package com.warehouse.geocoding.application.port.secondary;

import java.util.List;

import com.warehouse.geocoding.domain.vo.PostalCode;

public interface PostalCodeRepository {

    List<PostalCode> findByPostalCode(final String postalCode);

    List<PostalCode> findByPostalCodePrefix(final String postalCodePrefix);

    List<PostalCode> findByCityPrefix(final String cityPrefix);
}
