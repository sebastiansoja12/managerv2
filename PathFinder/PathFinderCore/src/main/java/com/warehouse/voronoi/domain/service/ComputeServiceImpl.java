package com.warehouse.voronoi.domain.service;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.Department;
import com.warehouse.voronoi.domain.model.VoronoiRequest;
import com.warehouse.voronoi.domain.port.secondary.VoronoiServicePort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@AllArgsConstructor
@Slf4j
public class ComputeServiceImpl implements ComputeService {
    
    private final VoronoiServicePort voronoiServicePort;

	private List<Department> calculateCoordinates(List<Department> departments) {
		departments.stream().filter(this::hasCity).forEach(depot -> {
			final Coordinates coordinates = voronoiServicePort.obtainCoordinates(depot.getCity());
			depot.setCoordinates(coordinates);
		});
		return departments;
	}

	private Coordinates calculateCoordinatesForRequestCity(String requestCity) {
		return voronoiServicePort.obtainCoordinates(requestCity);
	}

	private boolean hasCity(Department department) {
		return ObjectUtils.isNotEmpty(department) && StringUtils.isNotEmpty(department.getCity());
	}

	@Override
	public DepartmentCode calculateDepartmentCode(final VoronoiRequest request, final List<Department> departments) {
		final Map<String, Double> cities = new HashMap<>();
		final Coordinates coordinates = calculateCoordinatesForRequestCity(request.getCity());

		if (Objects.isNull(coordinates)) {
			return new DepartmentCode("");
		}

		final List<Department> depotsWithCoordinates = calculateCoordinates(departments);

		depotsWithCoordinates.forEach(depot -> {
			final double lon1 = depot.getCoordinates().getLon();
			final double lat1 = depot.getCoordinates().getLat();
			final double lat2 = coordinates.getLat();
			final double lon2 = coordinates.getLon();
			final double result = Math.pow((lat2 - lat1), 2) + Math.pow((lon2 - lon1), 2);
			cities.put(depot.getDepartmentCode(), result);
		});

		final String value =  Collections.min(cities.entrySet(), Map.Entry.comparingByValue()).getKey();
		return new DepartmentCode(value);
	}
}


