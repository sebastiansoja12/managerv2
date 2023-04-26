package com.warehouse.tsp.domain.service;

import com.warehouse.tsp.domain.model.Depot;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class CalculateDistanceServiceImpl implements CalculateDistanceService {

    private final CalculateDistanceBetweenDepots calculateDistanceBetweenDepots;

    @Override
    public double calculateDistance(List<Integer> route, List<Depot> depots) {
        double distance = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            int currentDepotIndex = route.get(i);
            int nextDepotIndex = route.get(i + 1);
            final Depot currentDepot = depots.get(currentDepotIndex);
            final Depot nextDepot = depots.get(nextDepotIndex);
            distance += calculateDistanceBetweenDepots.calculateDistanceBetweenDepots(currentDepot, nextDepot);
        }

        final int lastDepotIndex = route.get(route.size() - 1);
        final int firstDepotIndex = route.get(0);
        final Depot lastDepot = depots.get(lastDepotIndex);
        final Depot firstDepot = depots.get(firstDepotIndex);
        distance += calculateDistanceBetweenDepots.calculateDistanceBetweenDepots(lastDepot, firstDepot);

        return distance;
    }
}
