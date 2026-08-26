package com.warehouse.voronoi;

import java.util.List;

import com.warehouse.voronoi.dto.DepartmentDto;

public record VoronoiRequestDto(String city, String street, String zipCode, List<DepartmentDto> departments) {
}
