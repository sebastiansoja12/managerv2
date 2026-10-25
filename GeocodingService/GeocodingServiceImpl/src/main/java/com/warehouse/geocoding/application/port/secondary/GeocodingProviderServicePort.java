package com.warehouse.geocoding.application.port.secondary;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.geocoding.domain.vo.Coordinates;
import com.warehouse.geocoding.domain.vo.GeocodingAddress;
import com.warehouse.geocoding.domain.vo.GeocodingConfig;

public interface GeocodingProviderServicePort {

    boolean canHandle(final GeocodingProvider geocodingProvider);

    Coordinates obtainCoordinates(final GeocodingAddress address, final GeocodingConfig config);
}
