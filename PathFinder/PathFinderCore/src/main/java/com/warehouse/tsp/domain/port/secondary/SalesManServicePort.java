package com.warehouse.tsp.domain.port.secondary;

import com.warehouse.tsp.domain.model.Depot;

import java.util.List;

public interface SalesManServicePort {
    String findFastestRoute(List<Depot> depots);
}
