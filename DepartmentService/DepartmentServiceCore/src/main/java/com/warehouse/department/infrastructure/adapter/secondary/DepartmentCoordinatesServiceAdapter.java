package com.warehouse.department.infrastructure.adapter.secondary;

import com.warehouse.department.domain.port.secondary.DepartmentCoordinatesServicePort;
import com.warehouse.department.domain.vo.Address;
import com.warehouse.department.domain.vo.Coordinates;
import com.warehouse.voronoi.VoronoiCoordinatesService;
import com.warehouse.voronoi.VoronoiRequestDto;
import com.warehouse.voronoi.dto.CoordinatesDto;

import java.util.Collections;

public class DepartmentCoordinatesServiceAdapter implements DepartmentCoordinatesServicePort {

    private final VoronoiCoordinatesService voronoiCoordinatesService;

    public DepartmentCoordinatesServiceAdapter(final VoronoiCoordinatesService voronoiCoordinatesService) {
        this.voronoiCoordinatesService = voronoiCoordinatesService;
    }

    @Override
    public Coordinates getCoordinates(final Address address) {
        final VoronoiRequestDto request = new VoronoiRequestDto(address.city(), address.postalCode(),
                Collections.emptyList());
        final CoordinatesDto coordinates = this.voronoiCoordinatesService.findCoordinates(request);
        return new Coordinates(coordinates.latitude(), coordinates.longitude());
    }
}
