package com.warehouse.tsp.domain.port.primary;

import com.warehouse.tsp.domain.exception.MissingDepotsException;
import com.warehouse.tsp.domain.model.Depot;
import com.warehouse.tsp.domain.port.secondary.SalesManServicePort;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TravellingSalesManPortImpl implements TravellingSalesManPort {


    private final SalesManServicePort salesManServicePort;

    @Override
    public String findFastestRoute(List<Depot> depots) {
        if (depots.isEmpty()) {
            throw new MissingDepotsException("Depots cannot be empty");
        }
        return salesManServicePort.findFastestRoute(depots);
    }
}

