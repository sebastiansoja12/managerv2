package com.warehouse.tsp.infrastructure.adapter.primary;

import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.tsp.TSPService;
import com.warehouse.tsp.domain.model.Depot;
import com.warehouse.tsp.domain.port.primary.TravellingSalesManPort;
import com.warehouse.tsp.infrastructure.adapter.primary.mapper.TSPRequestMapper;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TSPServiceAdapter implements TSPService {

    private final TSPRequestMapper requestMapper;

    private final TravellingSalesManPort travellingSalesManPort;

    @Override
    public String findFastestRoute(List<DepotDto> depots) {
        final List<Depot> depotList = requestMapper.map(depots);
        return travellingSalesManPort.findFastestRoute(depotList);
    }
}

