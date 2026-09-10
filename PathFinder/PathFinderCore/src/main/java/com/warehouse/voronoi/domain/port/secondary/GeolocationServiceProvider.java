package com.warehouse.voronoi.domain.port.secondary;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.vo.GeocodingAddress;
import com.warehouse.voronoi.domain.vo.GeocodingConfig;

public interface GeolocationServiceProvider {

    boolean canHandle(final GeocodingProvider geocodingProvider);

    Coordinates obtainCoordinates(final GeocodingAddress address, final GeocodingConfig config);
}
