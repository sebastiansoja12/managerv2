package com.warehouse.voronoi;


public interface VoronoiService {

    VoronoiResponseDto findFastestRoute(final VoronoiRequestDto voronoiRequest);
}
