package com.warehouse.voronoi.domain.port.secondary;

import com.warehouse.voronoi.domain.model.Depot;

import java.util.List;

public interface VoronoiServicePort {

    String findFastestRoute(List<Depot> depots, String requestCity);
}
