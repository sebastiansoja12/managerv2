package com.warehouse.voronoi.domain.port.primary;

import com.warehouse.voronoi.domain.model.Depot;

import java.util.List;

public interface VoronoiPort {

    String findFastestRoute(List<Depot> depots, String requestCity);
}
