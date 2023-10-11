package com.warehouse.tsp.domain.service;

import com.warehouse.tsp.domain.model.Depot;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class ComputeServiceImpl implements ComputeService {

    private final CalculateDistanceService calculateDistanceService;

    private final RandomElementsSwapper randomElementsSwapper;

    @Override
    public String computeLength(List<Depot> depots) {

        final int numDepots = depots.size();
        List<Integer> bestRoute = null;
        double bestDistance = Double.MAX_VALUE;
        List<Integer> currentRoute = new ArrayList<>();
        for (int i = 0; i < numDepots; i++) {
            currentRoute.add(i);
        }
        Collections.shuffle(currentRoute);

        double currentDistance = calculateDistanceService.calculateDistance(currentRoute, depots);
        int numIterations = 0;
        while (numIterations < 10000) {
            List<Integer> newRoute = randomElementsSwapper.swapRandomElements(currentRoute);
            double newDistance = calculateDistanceService.calculateDistance(newRoute, depots);

            if (newDistance < currentDistance) {
                currentRoute = newRoute;
                currentDistance = newDistance;
            }

            if (currentDistance < bestDistance) {
                bestRoute = currentRoute;
                bestDistance = currentDistance;
            }

            numIterations++;
        }
        final List<String> bestDepotCodes = new ArrayList<>();

        for (int i = 0; i < numDepots; i++) {
            bestDepotCodes.add(depots.get(bestRoute.get(i)).getDepotCode());
        }

        return bestDepotCodes + " " + "\nTotal distance: " +
                calculateDistanceService.calculateDistance(bestRoute, depots) + "km";
    }
}
