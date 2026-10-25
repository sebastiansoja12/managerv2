package com.warehouse.voronoi;

import com.warehouse.voronoi.dto.AddressDto;
import com.warehouse.voronoi.dto.CoordinatesDto;
import com.warehouse.voronoi.dto.DepartmentDto;

import java.util.List;

public record VoronoiRequestDto(AddressDto address, CoordinatesDto coordinatesOfGivenLocation, List<DepartmentDto> departments) {


    public String city() {
        return address.city();
    }
}
