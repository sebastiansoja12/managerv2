package com.warehouse.voronoi;


import com.warehouse.dto.DepotDto;

import java.util.List;

public interface VoronoiService {

    String findFastestRoute(List<DepotDto> depotRequestList, String requestCity);
}
