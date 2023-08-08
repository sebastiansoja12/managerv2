package com.warehouse.voronoi.infrastructure.adapter.primary;

import com.warehouse.dto.DepotDto;
import com.warehouse.voronoi.VoronoiService;
import com.warehouse.voronoi.domain.model.Depot;
import com.warehouse.voronoi.domain.port.primary.VoronoiPort;
import com.warehouse.voronoi.infrastructure.adapter.primary.mapper.AddressRequestMapper;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class VoronoiServiceAdapter implements VoronoiService {

    private final VoronoiPort voronoiPort;

    private final AddressRequestMapper addressRequestMapper;

    @Override
    public String findFastestRoute(List<DepotDto> depotsRequestList, String requestCity) {
        final List<Depot> depots = addressRequestMapper.map(depotsRequestList);
        return voronoiPort.findFastestRoute(depots, requestCity);
    }
}

