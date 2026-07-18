package com.warehouse.voronoi.domain.port.secondary;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.vo.GeocodingConfig;

public interface GeolocationServiceProvider {

    boolean canHandle(final GeocodingProvider geocodingProvider);

    Coordinates obtainCoordinates(final String requestCity, final GeocodingConfig config);
}
