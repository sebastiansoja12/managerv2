package com.warehouse.voronoi;

import com.warehouse.voronoi.dto.CoordinatesDto;

public interface VoronoiCoordinatesService {
    CoordinatesDto findCoordinates(final VoronoiRequestDto voronoiRequest);
}
