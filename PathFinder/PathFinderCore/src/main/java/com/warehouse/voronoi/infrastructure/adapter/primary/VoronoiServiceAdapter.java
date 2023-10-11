package com.warehouse.voronoi.infrastructure.adapter.primary;

import com.warehouse.voronoi.VoronoiService;
import com.warehouse.voronoi.domain.port.primary.VoronoiPort;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VoronoiServiceAdapter implements VoronoiService {

    private final VoronoiPort voronoiPort;

    @Override
    public String findFastestRoute(String requestCity) {
        return voronoiPort.findFastestRoute(requestCity);
    }
}

