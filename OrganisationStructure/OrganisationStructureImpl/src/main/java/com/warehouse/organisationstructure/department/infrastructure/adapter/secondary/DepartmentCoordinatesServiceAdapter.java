package com.warehouse.organisationstructure.department.infrastructure.adapter.secondary;

import com.warehouse.organisationstructure.department.application.port.secondary.DepartmentCoordinatesServicePort;
import com.warehouse.organisationstructure.department.domain.vo.Address;
import com.warehouse.organisationstructure.department.domain.vo.Coordinates;
import com.warehouse.geocoding.api.GeocodingAddressDto;
import com.warehouse.geocoding.api.GeocodingCoordinatesDto;
import com.warehouse.geocoding.api.GeocodingCoordinatesService;

public class DepartmentCoordinatesServiceAdapter implements DepartmentCoordinatesServicePort {

    private final GeocodingCoordinatesService geocodingCoordinatesService;

    public DepartmentCoordinatesServiceAdapter(final GeocodingCoordinatesService geocodingCoordinatesService) {
        this.geocodingCoordinatesService = geocodingCoordinatesService;
    }

    @Override
    public Coordinates getCoordinates(final Address address) {
        final GeocodingAddressDto request = new GeocodingAddressDto(address.city(), address.street(), address.postalCode());
        final GeocodingCoordinatesDto coordinates = this.geocodingCoordinatesService.findCoordinates(request);
        return new Coordinates(coordinates.latitude(), coordinates.longitude());
    }
}
