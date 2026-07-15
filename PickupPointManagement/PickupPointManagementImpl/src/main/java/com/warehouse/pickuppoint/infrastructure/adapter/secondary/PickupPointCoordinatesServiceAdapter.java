package com.warehouse.pickuppoint.infrastructure.adapter.secondary;

import com.warehouse.pickuppoint.application.port.secondary.PickupPointCoordinatesServicePort;
import com.warehouse.pickuppoint.domain.vo.GeoCoordinates;
import com.warehouse.pickuppoint.domain.vo.PickupPointAddress;
import com.warehouse.voronoi.VoronoiCoordinatesService;
import com.warehouse.voronoi.VoronoiRequestDto;
import com.warehouse.voronoi.dto.CoordinatesDto;

import java.util.List;

public class PickupPointCoordinatesServiceAdapter implements PickupPointCoordinatesServicePort {

    private final VoronoiCoordinatesService coordinatesService;

    public PickupPointCoordinatesServiceAdapter(final VoronoiCoordinatesService coordinatesService) {
        this.coordinatesService = coordinatesService;
    }

    @Override
    public GeoCoordinates getCoordinates(final PickupPointAddress address) {
        final String street = address.street() + " " + address.buildingNumber();
        final VoronoiRequestDto request = new VoronoiRequestDto(
                address.city(), street, address.postalCode(), List.of());
        final CoordinatesDto coordinates = this.coordinatesService.findCoordinates(request);
        return new GeoCoordinates(coordinates.latitude(), coordinates.longitude());
    }
}
