package com.warehouse.voronoi.infrastructure.adapter.secondary;

import com.warehouse.depot.domain.port.primary.DepotPort;
import com.warehouse.voronoi.domain.model.Depot;
import com.warehouse.voronoi.domain.port.secondary.DepotServicePort;
import com.warehouse.voronoi.infrastructure.adapter.secondary.mapper.DepotResponseMapper;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DepotServiceAdapter implements DepotServicePort {

    private final DepotPort depotPort;

    private final DepotResponseMapper depotResponseMapper;

    @Override
    public List<Depot> downloadDepots() {
        final List<com.warehouse.depot.domain.vo.Depot> depots = depotPort.findAll();
        return depotResponseMapper.map(depots);
    }
}
