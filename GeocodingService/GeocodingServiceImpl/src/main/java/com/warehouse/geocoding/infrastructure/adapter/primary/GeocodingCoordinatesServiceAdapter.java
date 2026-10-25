package com.warehouse.geocoding.infrastructure.adapter.primary;

import com.warehouse.geocoding.api.GeocodingAddressDto;
import com.warehouse.geocoding.api.GeocodingCoordinatesDto;
import com.warehouse.geocoding.api.GeocodingCoordinatesService;
import com.warehouse.geocoding.application.port.primary.GeocodingCoordinatesPort;
import com.warehouse.geocoding.domain.vo.Coordinates;
import com.warehouse.geocoding.domain.vo.GeocodingAddress;

public class GeocodingCoordinatesServiceAdapter implements GeocodingCoordinatesService {

    private final GeocodingCoordinatesPort geocodingCoordinatesPort;

    public GeocodingCoordinatesServiceAdapter(final GeocodingCoordinatesPort geocodingCoordinatesPort) {
        this.geocodingCoordinatesPort = geocodingCoordinatesPort;
    }

    @Override
    public GeocodingCoordinatesDto findCoordinates(final GeocodingAddressDto address) {
        final Coordinates coordinates = geocodingCoordinatesPort.obtainCoordinates(
                new GeocodingAddress(address.city(), address.street(), address.postalCode()));
        return new GeocodingCoordinatesDto(coordinates.lat(), coordinates.lon());
    }
}
