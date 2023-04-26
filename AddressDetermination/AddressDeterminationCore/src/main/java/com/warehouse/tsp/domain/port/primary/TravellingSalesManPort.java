package com.warehouse.tsp.domain.port.primary;

import com.warehouse.tsp.domain.model.Depot;

import java.util.List;

public interface TravellingSalesManPort {

    String findFastestRoute(List<Depot> depots);
}
