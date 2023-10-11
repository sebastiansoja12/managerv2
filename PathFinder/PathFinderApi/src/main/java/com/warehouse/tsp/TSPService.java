package com.warehouse.tsp;


import com.warehouse.dto.DepotDto;

import java.util.List;

public interface TSPService {
    String findFastestRoute(List<DepotDto> depots);
}
