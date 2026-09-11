package com.warehouse.geocoding.application.port.primary;

import com.warehouse.geocoding.domain.vo.Coordinates;
import com.warehouse.geocoding.domain.vo.GeocodingAddress;

public interface GeocodingCoordinatesPort {

    Coordinates obtainCoordinates(final GeocodingAddress address);
}
