package com.warehouse.tsp;

import com.warehouse.depot.api.dto.DepotDto;

import java.util.List;

public interface TSPService {
    String findFastestRoute(List<DepotDto> depots);
}
