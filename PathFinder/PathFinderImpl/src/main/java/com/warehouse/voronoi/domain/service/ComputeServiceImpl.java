package com.warehouse.voronoi.domain.service;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.Department;
import com.warehouse.voronoi.domain.model.VoronoiRequest;

import java.util.*;

public class ComputeServiceImpl implements ComputeService {

	@Override
	public DepartmentCode calculateNearestDepartment(final VoronoiRequest request, final List<Department> departments) {
		final Map<String, Double> cities = new HashMap<>();
		final Coordinates coordinates = request.getCoordinates();

		if (Objects.isNull(coordinates)) {
			return new DepartmentCode("");
		}

		departments.forEach(depot -> {
			final double lon1 = depot.getCoordinates().lon();
			final double lat1 = depot.getCoordinates().lat();
			final double lat2 = coordinates.lat();
			final double lon2 = coordinates.lon();
			final double result = Math.pow((lat2 - lat1), 2) + Math.pow((lon2 - lon1), 2);
			cities.put(depot.getDepartmentCode(), result);
		});

		final String value = Collections.min(cities.entrySet(), Map.Entry.comparingByValue()).getKey();
		return new DepartmentCode(value);
	}
}
