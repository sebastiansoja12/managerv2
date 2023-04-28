package com.warehouse.star.domain.port.primary;

import com.warehouse.star.domain.exception.MissingCoordinatesException;
import com.warehouse.star.domain.exception.MissingDepotsException;
import com.warehouse.star.domain.model.Coordinates;
import com.warehouse.star.domain.model.Depot;
import com.warehouse.star.domain.service.CalculateDistanceBetweenDepots;
import lombok.AllArgsConstructor;

import java.util.*;

@AllArgsConstructor
public class StarPortImpl implements StarPort {

    public static final Integer NUM_NEIGHBORS = 2;

    private static final double EARTH_RADIUS_KM = 6371;

    public static final double CIRCUMFERENCE_OF_THE_EQUATOR = 40075.704;

    public static final int ROTATION_OF_EARTH = 360;

    public static final double PI = 3.14;

    private final CalculateDistanceBetweenDepots calculateDistance;

    @Override
    public String starPathFinder(String depotCode, List<Depot> depots, Coordinates coordinates) {
        validateRequest(depotCode, depots, coordinates);

        final Depot depot = depots.stream()
                .filter(d -> d.getDepotCode(depotCode))
                .findFirst()
                .orElse(null);

        final Depot destDepot = depots.stream()
                .filter(d -> d.getCoordinates().getLat() == coordinates.getLat()
                        && d.getCoordinates().getLon() == coordinates.getLon())
                .findFirst()
                .orElse(null);

        return find(depot, destDepot, depots);
    }

    public static String find(Depot start, Depot goal, List<Depot> depots) {
        final Map<Depot, Depot> cameFrom = new HashMap<>();
        final Map<Depot, Double> gScore = new HashMap<>();
        final Map<Depot, Double> fScore = new HashMap<>();
        final Set<Depot> visited = new HashSet<>();
        final Set<Depot> open = new HashSet<>();
        for (Depot depot : depots) {
            gScore.put(depot, Double.MAX_VALUE);
            fScore.put(depot, Double.MAX_VALUE);
        }
        gScore.put(start, 0.0);
        fScore.put(start, calculateDistance(start, goal));

        open.add(start);

        while (!open.isEmpty()) {
            final Depot current = Collections.min(open, Comparator.comparingDouble(fScore::get));
            if (current.equals(goal)) {
                return reconstructPath(cameFrom, goal).toString();
            }
            open.remove(current);
            visited.add(current);
            for (Depot neighbor : getNeighbors(current, depots)) {
                if (visited.contains(neighbor)) {
                    continue;
                }
                final double tentativeGScore = gScore.get(current) + calculateDistance(current, neighbor);
                if (!open.contains(neighbor)) {
                    open.add(neighbor);
                } else if (tentativeGScore <= gScore.get(neighbor)) {
                    continue;
                }
                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, tentativeGScore + calculateDistance(neighbor, goal));
            }
        }
        return null;
    }


    private static StringBuilder reconstructPath(Map<Depot, Depot> cameFrom, Depot current) {
        if (!cameFrom.containsKey(current)) {
            final StringBuilder path = new StringBuilder();
            path.append(current.getDepotCode());
            return path;
        }

        final Depot previous = cameFrom.get(current);
        final StringBuilder path = reconstructPath(cameFrom, previous);
        path.append(" \n↓ ").append("\n").append(current.getDepotCode());

        return path;
    }

    private static double calculateDistance(Depot a, Depot b) {
        final double lat1 = Math.toRadians(a.getCoordinates().getLat());
        final double lon1 = Math.toRadians(a.getCoordinates().getLon());
        final double lat2 = Math.toRadians(b.getCoordinates().getLat());
        final double lon2 = Math.toRadians(b.getCoordinates().getLon());

        final double deltaLat = lat2 - lat1;
        final double deltaLon = lon2 - lon1;

        final double aHarv = Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(deltaLon / 2), 2);
        final double cHarv = 2 * Math.atan2(Math.sqrt(aHarv), Math.sqrt(1 - aHarv));

        return EARTH_RADIUS_KM * cHarv;
    }

    private static List<Depot> getNeighbors(Depot depot, List<Depot> depots) {
        final List<Depot> neighbors = new ArrayList<>();
        for (Depot value : depots) {
            if (value.equals(depot)) {
                continue;
            }
            final double distance = calculateDistance(depot, value);
            if (neighbors.size() <= NUM_NEIGHBORS) {
                neighbors.add(value);
            } else {
                // replace farthest neighbor with closer one
                double farthestDistance = 0;
                int farthestIndex = -1;
                for (int j = 0; j < neighbors.size(); j++) {
                    double neighborDistance = calculateDistance(depot, neighbors.get(j));
                    if (neighborDistance > farthestDistance) {
                        farthestDistance = neighborDistance;
                        farthestIndex = j;
                    }
                }
                if (distance < farthestDistance) {
                    neighbors.set(farthestIndex, value);
                }
            }
        }
        return neighbors;
    }


    private void validateRequest(String depotCode, List<Depot> depots, Coordinates coordinates) {
        if (depots.isEmpty()) {
            throw new MissingDepotsException("Depots cannot be empty");
        } else if (coordinates == null) {
            throw new MissingCoordinatesException("Request city is empty");
        } else if (depotCode == null) {
            throw new MissingDepotsException("Depot code cannot be null!");
        }
    }
}
