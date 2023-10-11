package com.warehouse.voronoi.domain.port.secondary;

import com.warehouse.voronoi.domain.model.Coordinates;

public interface VoronoiServicePort {

    Coordinates obtainCoordinates(String requestCity);
}
