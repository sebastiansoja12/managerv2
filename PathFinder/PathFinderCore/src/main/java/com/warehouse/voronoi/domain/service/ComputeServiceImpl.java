package com.warehouse.voronoi.domain.service;

import java.util.*;

import com.warehouse.voronoi.domain.exception.MissingCoordinatesException;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.Depot;
import com.warehouse.voronoi.domain.port.secondary.VoronoiServicePort;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@Slf4j
public class ComputeServiceImpl implements ComputeService {
    
    private final VoronoiServicePort voronoiServicePort;
	
    @Override
    public String calculate(String requestCity, List<Depot> depots) {
        final Map<String, Double> cities = new HashMap<>();
		final Coordinates coordinates = calculateCoordinatesForRequestCity(requestCity);

		if (Objects.isNull(coordinates)) {
			throw new MissingCoordinatesException("Coordinates missing");
		}
		
		final List<Depot> depotsWithCoordinates = calculateCoordinates(depots);
		
		depotsWithCoordinates.forEach(depot -> {
			final double lon1 = depot.getCoordinates().getLon();
			final double lat1 = depot.getCoordinates().getLat();
			final double lat2 = coordinates.getLat();
			final double lon2 = coordinates.getLon();
			final double result = Math.pow((lat2 - lat1), 2) + Math.pow((lon2 - lon1), 2);
			cities.put(depot.getDepotCode(), result);
		});

        return Collections.min(cities.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

	private List<Depot> calculateCoordinates(List<Depot> depots) {
		depots.stream().filter(this::hasCity).forEach(depot -> {
			final Coordinates coordinates = voronoiServicePort.obtainCoordinates(depot.getCity());
			depot.setCoordinates(coordinates);
		});
		return depots;
	}

	private Coordinates calculateCoordinatesForRequestCity(String requestCity) {
		return voronoiServicePort.obtainCoordinates(requestCity);
	}

	private boolean hasCity(Depot depot) {
		return ObjectUtils.isNotEmpty(depot) && StringUtils.isNotEmpty(depot.getCity());
	}
}


