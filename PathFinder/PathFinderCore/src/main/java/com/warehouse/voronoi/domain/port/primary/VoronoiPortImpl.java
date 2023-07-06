package com.warehouse.voronoi.domain.port.primary;

import com.warehouse.voronoi.domain.exception.MissingDepotsException;
import com.warehouse.voronoi.domain.exception.MissingRequestCityException;
import com.warehouse.voronoi.domain.model.Depot;
import com.warehouse.voronoi.domain.port.secondary.VoronoiServicePort;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class VoronoiPortImpl implements VoronoiPort {

    private final VoronoiServicePort determinationServicePort;
    @Override
    public String findFastestRoute(List<Depot> depots, String requestCity) {
        validateRequest(depots, requestCity);
        return determinationServicePort.findFastestRoute(depots, requestCity);
    }

    private void validateRequest(List<Depot> depots, String requestCity) {
        if (depots.isEmpty()) {
            throw new MissingDepotsException("Depots cannot be empty");
        } else if (requestCity == null) {
            throw new MissingRequestCityException("Request city is empty");
        }
    }
}
