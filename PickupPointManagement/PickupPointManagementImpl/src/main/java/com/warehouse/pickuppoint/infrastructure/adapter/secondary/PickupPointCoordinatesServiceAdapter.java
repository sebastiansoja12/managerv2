package com.warehouse.pickuppoint.infrastructure.adapter.secondary;

import com.warehouse.pickuppoint.application.port.secondary.PickupPointCoordinatesServicePort;
import com.warehouse.pickuppoint.domain.vo.GeoCoordinates;
import com.warehouse.pickuppoint.domain.vo.PickupPointAddress;
import com.warehouse.geocoding.api.GeocodingAddressDto;
import com.warehouse.geocoding.api.GeocodingCoordinatesDto;
import com.warehouse.geocoding.api.GeocodingCoordinatesService;

public class PickupPointCoordinatesServiceAdapter implements PickupPointCoordinatesServicePort {

    private final GeocodingCoordinatesService coordinatesService;

    public PickupPointCoordinatesServiceAdapter(final GeocodingCoordinatesService coordinatesService) {
        this.coordinatesService = coordinatesService;
    }

    @Override
    public GeoCoordinates getCoordinates(final PickupPointAddress address) {
        final String street = address.street() + " " + address.buildingNumber();
        final GeocodingAddressDto request = new GeocodingAddressDto(
                address.city(), street, address.postalCode());
        final GeocodingCoordinatesDto coordinates = this.coordinatesService.findCoordinates(request);
        return new GeoCoordinates(coordinates.latitude(), coordinates.longitude());
    }
}
