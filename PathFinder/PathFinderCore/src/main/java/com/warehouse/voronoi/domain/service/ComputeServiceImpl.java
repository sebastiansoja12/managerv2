package com.warehouse.voronoi.domain.service;

import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.voronoi.domain.model.Depot;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@AllArgsConstructor
@Slf4j
public class ComputeServiceImpl implements ComputeService {

    @Override
    public String computeLength(Coordinates coordinates, List<Depot> depots) {
        log.info("Computing fastest route for coordinates: " +
                coordinates.getLat() + ", " + coordinates.getLon());
        final Map<String, Double> cities = new HashMap<>();
        for (Depot depot: depots) {
            final double lon1 = depot.getCoordinates().getLon();
            final double lat1 = depot.getCoordinates().getLat();
            final double lat2 = coordinates.getLat();
            final double lon2 = coordinates.getLon();

            final double result = Math.pow((lat2 - lat1), 2) +  Math.pow((lon2 - lon1), 2);

            cities.put(depot.getDepotCode(), result);
        }
        return Collections.min(cities.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}


