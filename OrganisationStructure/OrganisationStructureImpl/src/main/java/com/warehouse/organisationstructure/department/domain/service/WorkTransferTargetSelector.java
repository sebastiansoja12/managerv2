package com.warehouse.organisationstructure.department.domain.service;

import com.warehouse.organisationstructure.department.domain.enumeration.DepartmentType;
import com.warehouse.organisationstructure.department.domain.model.Department;
import com.warehouse.organisationstructure.department.domain.vo.Coordinates;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class WorkTransferTargetSelector {

    private static final double EARTH_RADIUS_IN_KILOMETERS = 6371.0;

    public Optional<Department> select(final Department source, final List<Department> candidates) {
        final boolean sourceIsSortingFacility = source.getDepartmentType() == DepartmentType.SORTING_FACILITY;
        return candidates.stream()
                .filter(Department::isActive)
                .filter(candidate -> !Objects.equals(candidate.getDepartmentId(), source.getDepartmentId()))
                .filter(candidate -> candidate.getCountryCode() == source.getCountryCode())
                .filter(candidate -> (candidate.getDepartmentType() == DepartmentType.SORTING_FACILITY)
                        == sourceIsSortingFacility)
                .filter(candidate -> hasLocation(candidate.getCoordinates()))
                .filter(candidate -> hasLocation(source.getCoordinates()))
                .min(Comparator.comparingDouble(candidate ->
                        distanceInKilometers(source.getCoordinates(), candidate.getCoordinates())));
    }

    private boolean hasLocation(final Coordinates coordinates) {
        return coordinates != null && coordinates.lat() != null && coordinates.lon() != null;
    }

    private double distanceInKilometers(final Coordinates first, final Coordinates second) {
        final double latitudeDelta = Math.toRadians(second.lat() - first.lat());
        final double longitudeDelta = Math.toRadians(second.lon() - first.lon());
        final double haversine = Math.pow(Math.sin(latitudeDelta / 2), 2)
                + Math.cos(Math.toRadians(first.lat())) * Math.cos(Math.toRadians(second.lat()))
                * Math.pow(Math.sin(longitudeDelta / 2), 2);
        return 2 * EARTH_RADIUS_IN_KILOMETERS * Math.asin(Math.sqrt(haversine));
    }
}
