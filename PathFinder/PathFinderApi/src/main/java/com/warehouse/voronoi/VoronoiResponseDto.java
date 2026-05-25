package com.warehouse.voronoi;

import com.warehouse.voronoi.dto.DepartmentCodeDto;

public record VoronoiResponseDto(DepartmentCodeDto departmentCode, String city) {
}
