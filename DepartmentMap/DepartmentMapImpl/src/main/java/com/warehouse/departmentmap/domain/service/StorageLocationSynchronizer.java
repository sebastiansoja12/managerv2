package com.warehouse.departmentmap.domain.service;

import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.departmentmap.domain.exception.DepartmentMapException;
import com.warehouse.departmentmap.domain.model.DepartmentMap;
import com.warehouse.departmentmap.domain.model.StorageLocation;
import com.warehouse.departmentmap.domain.vo.StorageLocationDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StorageLocationSynchronizer {

    public Plan plan(final DepartmentMap departmentMap, final List<StorageLocation> existingLocations) {
        final Map<MapElementId, StorageLocationDefinition> definitions = departmentMap.storageLocationDefinitions()
                .stream()
                .collect(Collectors.toMap(StorageLocationDefinition::locationId, Function.identity()));
        final List<StorageLocation> removed = new ArrayList<>();
        final List<StorageLocation> changed = new ArrayList<>();
        for (final StorageLocation location : existingLocations) {
            final StorageLocationDefinition definition = definitions.remove(location.getLocationId());
            if (definition == null) {
                if (!location.isEmpty()) {
                    throw DepartmentMapException.conflict("LOCATION_NOT_EMPTY", "Location " + location.getCode()
                            + " still holds pallets and cannot be removed from the map");
                }
                removed.add(location);
            } else if (location.differsFrom(definition)) {
                location.redefine(definition);
                changed.add(location);
            }
        }
        final List<StorageLocation> created = definitions.values().stream()
                .map(definition -> StorageLocation.define(definition, departmentMap.getDepartmentMapId(),
                        departmentMap.getOperatorId()))
                .toList();
        return new Plan(created, changed, removed);
    }

    public record Plan(List<StorageLocation> created, List<StorageLocation> changed, List<StorageLocation> removed) {
    }
}
