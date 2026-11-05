package com.warehouse.geocoding.application.port.secondary;

import java.util.Optional;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.geocoding.domain.vo.Coordinates;
import com.warehouse.geocoding.domain.vo.NormalizedGeocodingAddress;

public interface GeocodingAddressRepository {

    Optional<Coordinates> findCoordinates(final NormalizedGeocodingAddress address);

    void saveIfAbsent(final NormalizedGeocodingAddress address, final Coordinates coordinates,
                      final GeocodingProvider provider);
}
