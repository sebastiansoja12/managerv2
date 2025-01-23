package com.warehouse.voronoi.domain.port.primary;

import com.warehouse.voronoi.domain.model.VoronoiRequest;
import com.warehouse.voronoi.domain.vo.VoronoiResponse;

public interface VoronoiPort {

    VoronoiResponse findFastestRoute(final VoronoiRequest request);
}
