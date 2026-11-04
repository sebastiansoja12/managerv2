package com.warehouse.logisticsdelivery.domain.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentCapacity;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentLoad;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.ToLongFunction;

public class DepartmentCapacityBalancer {

    private static final double EARTH_RADIUS_IN_KILOMETERS = 6371.0;

    public DepartmentLoad load(final DepartmentCapacity department, final long deliveriesToday) {
        final int dailyCapacity = department.dailyCapacity();
        final int utilizationPercent = dailyCapacity <= 0 ? 0 : (int) Math.min(Integer.MAX_VALUE,
                deliveriesToday * 100 / dailyCapacity);
        return new DepartmentLoad(department.departmentId(), deliveriesToday, dailyCapacity, utilizationPercent,
                dailyCapacity > 0 && utilizationPercent >= department.warningThresholdPercent(),
                dailyCapacity > 0 && deliveriesToday >= dailyCapacity);
    }

    public DepartmentId allocate(final DepartmentId resolvedDepartmentId,
                                 final List<DepartmentCapacity> departments,
                                 final ToLongFunction<DepartmentId> deliveriesToday) {
        final Optional<DepartmentCapacity> resolved = departments.stream()
                .filter(department -> department.departmentId().equals(resolvedDepartmentId))
                .findFirst();
        if (resolved.isEmpty() || !resolved.get().redirectOverflowToNeighbour() || !resolved.get().hasLocation()
                || hasFreeCapacity(resolved.get(), deliveriesToday)) {
            return resolvedDepartmentId;
        }
        final DepartmentCapacity overloaded = resolved.get();
        return departments.stream()
                .filter(DepartmentCapacity::active)
                .filter(DepartmentCapacity::hasLocation)
                .filter(candidate -> !candidate.departmentId().equals(resolvedDepartmentId))
                .sorted(Comparator.comparingDouble(candidate -> distanceInKilometers(overloaded, candidate)))
                .filter(candidate -> hasFreeCapacity(candidate, deliveriesToday))
                .map(DepartmentCapacity::departmentId)
                .findFirst()
                .orElse(resolvedDepartmentId);
    }

    private boolean hasFreeCapacity(final DepartmentCapacity department,
                                    final ToLongFunction<DepartmentId> deliveriesToday) {
        return department.dailyCapacity() <= 0
                || deliveriesToday.applyAsLong(department.departmentId()) < department.dailyCapacity();
    }

    private double distanceInKilometers(final DepartmentCapacity first, final DepartmentCapacity second) {
        final double latitudeDelta = Math.toRadians(second.latitude() - first.latitude());
        final double longitudeDelta = Math.toRadians(second.longitude() - first.longitude());
        final double haversine = Math.pow(Math.sin(latitudeDelta / 2), 2)
                + Math.cos(Math.toRadians(first.latitude())) * Math.cos(Math.toRadians(second.latitude()))
                * Math.pow(Math.sin(longitudeDelta / 2), 2);
        return 2 * EARTH_RADIUS_IN_KILOMETERS * Math.asin(Math.sqrt(haversine));
    }
}
