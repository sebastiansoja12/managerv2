package com.warehouse.geocoding.api;

public interface GeocodingCoordinatesService {

    GeocodingCoordinatesDto findCoordinates(final GeocodingAddressDto address);
}
