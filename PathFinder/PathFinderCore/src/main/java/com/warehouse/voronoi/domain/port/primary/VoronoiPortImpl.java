package com.warehouse.voronoi.domain.port.primary;

import java.util.List;

import com.warehouse.voronoi.domain.exception.MissingDepotsException;
import com.warehouse.voronoi.domain.exception.MissingRequestCityException;
import com.warehouse.voronoi.domain.model.Depot;
import com.warehouse.voronoi.domain.port.secondary.DepotServicePort;
import com.warehouse.voronoi.domain.service.ComputeService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VoronoiPortImpl implements VoronoiPort {

    private final DepotServicePort depotServicePort;

    private final ComputeService computeService;

    @Override
    public String findFastestRoute(String requestCity) {
        final List<Depot> depots = depotServicePort.downloadDepots();
        validateRequest(depots, requestCity);
        return computeService.calculate(requestCity, depots);
    }

    private void validateRequest(List<Depot> depots, String requestCity) {
        if (depots.isEmpty()) {
            throw new MissingDepotsException("Depots cannot be empty");
        } else if (requestCity == null) {
            throw new MissingRequestCityException("Request city is empty");
        }
    }
}
