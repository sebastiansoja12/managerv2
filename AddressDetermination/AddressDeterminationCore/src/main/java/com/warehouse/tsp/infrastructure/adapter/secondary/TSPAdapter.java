package com.warehouse.tsp.infrastructure.adapter.secondary;

import com.warehouse.tsp.domain.model.Depot;
import com.warehouse.tsp.domain.port.secondary.SalesManServicePort;
import com.warehouse.tsp.domain.service.ComputeService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TSPAdapter implements SalesManServicePort {

    private final ComputeService computeService;

    @Override
    public String findFastestRoute(List<Depot> depots) {
        return computeService.computeLength(depots);
    }
}
