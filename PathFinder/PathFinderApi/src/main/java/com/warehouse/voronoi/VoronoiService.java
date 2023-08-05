package com.warehouse.voronoi;

import com.warehouse.depot.api.dto.DepotDto;

import java.util.List;

public interface VoronoiService {

    String findFastestRoute(List<DepotDto> depotRequestList, String requestCity);
}
