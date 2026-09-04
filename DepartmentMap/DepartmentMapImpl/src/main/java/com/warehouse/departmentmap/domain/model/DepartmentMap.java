package com.warehouse.departmentmap.domain.model;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.departmentmap.domain.exception.DepartmentMapException;
import com.warehouse.departmentmap.domain.vo.StorageLocationDefinition;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class DepartmentMap {

    public static final int MAX_ELEMENTS = 5000;
    private static final double MIN_DIMENSION = 1;
    private static final double MAX_DIMENSION = 5000;
    private static final double MIN_GRID_SIZE = 0.1;
    private static final double MAX_GRID_SIZE = 50;
    private static final int MAX_NAME_LENGTH = 120;

    private final DepartmentMapId departmentMapId;
    private final OperatorId operatorId;
    private final DepartmentId departmentId;
    private final Instant createdAt;
    private final Long version;
    private String name;
    private double width;
    private double height;
    private double gridSize;
    private List<MapElement> elements;
    private Instant updatedAt;

    public DepartmentMap(final DepartmentMapId departmentMapId,
                         final OperatorId operatorId,
                         final DepartmentId departmentId,
                         final String name,
                         final double width,
                         final double height,
                         final double gridSize,
                         final List<MapElement> elements,
                         final Long version,
                         final Instant createdAt,
                         final Instant updatedAt) {
        this.departmentMapId = departmentMapId;
        this.operatorId = operatorId;
        this.departmentId = departmentId;
        this.name = name;
        this.width = width;
        this.height = height;
        this.gridSize = gridSize;
        this.elements = List.copyOf(elements);
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DepartmentMap create(final OperatorId operatorId,
                                       final DepartmentId departmentId,
                                       final String name,
                                       final double width,
                                       final double height,
                                       final double gridSize,
                                       final Instant now) {
        if (departmentId == null || departmentId.getValue() == null) {
            throw DepartmentMapException.invalid("Department id is required");
        }
        validateDimensions(width, height, gridSize);
        return new DepartmentMap(DepartmentMapId.generate(), operatorId,
                departmentId, normalizeName(name), width, height, gridSize,
                List.of(), null, now, now);
    }

    public void changeLayout(final String name,
                             final double width,
                             final double height,
                             final double gridSize,
                             final List<MapElement> elements,
                             final Instant now) {
        validateDimensions(width, height, gridSize);
        if (elements == null) {
            throw DepartmentMapException.invalid("Map elements are required");
        }
        if (elements.size() > MAX_ELEMENTS) {
            throw DepartmentMapException.invalid("Map may contain at most " + MAX_ELEMENTS + " elements");
        }
        final Set<MapElementId> elementIds = new HashSet<>();
        final Set<String> locationCodes = new HashSet<>();
        for (final MapElement element : elements) {
            if (!elementIds.add(element.getElementId())) {
                throw DepartmentMapException.invalid("Element id " + element.getElementId().value() + " is duplicated");
            }
            if (!element.getGeometry().centerWithin(width, height)) {
                throw DepartmentMapException.invalid("Element " + describe(element) + " lies outside the map");
            }
            if (element.isStorageLocation() && !locationCodes.add(element.getLocationCode())) {
                throw DepartmentMapException.invalid("Storage location code " + element.getLocationCode()
                        + " is used more than once");
            }
        }
        this.name = normalizeName(name);
        this.width = width;
        this.height = height;
        this.gridSize = gridSize;
        this.elements = List.copyOf(elements);
        this.updatedAt = now;
    }

    public void verifyVersion(final Long expectedVersion) {
        if (expectedVersion == null) {
            throw DepartmentMapException.invalid("Map version is required");
        }
        if (!Objects.equals(this.version, expectedVersion)) {
            throw DepartmentMapException.conflict("MAP_VERSION_CONFLICT",
                    "Map " + name + " was changed by another user. Reload the map and apply your changes again");
        }
    }

    public List<StorageLocationDefinition> storageLocationDefinitions() {
        return elements.stream()
                .map(MapElement::storageLocationDefinition)
                .flatMap(Optional::stream)
                .toList();
    }

    public DepartmentMapId getDepartmentMapId() {
        return departmentMapId;
    }

    public OperatorId getOperatorId() {
        return operatorId;
    }

    public DepartmentId getDepartmentId() {
        return departmentId;
    }

    public String getName() {
        return name;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getGridSize() {
        return gridSize;
    }

    public List<MapElement> getElements() {
        return elements;
    }

    public Long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private static String describe(final MapElement element) {
        return element.getLabel() != null ? element.getLabel()
                : element.getLocationCode() != null ? element.getLocationCode()
                : element.getElementId().value().toString();
    }

    private static String normalizeName(final String name) {
        if (name == null || name.isBlank()) {
            throw DepartmentMapException.invalid("Map name is required");
        }
        final String normalized = name.trim();
        if (normalized.length() > MAX_NAME_LENGTH) {
            throw DepartmentMapException.invalid("Map name must not exceed " + MAX_NAME_LENGTH + " characters");
        }
        return normalized;
    }

    private static void validateDimensions(final double width, final double height, final double gridSize) {
        if (!inRange(width, MIN_DIMENSION, MAX_DIMENSION) || !inRange(height, MIN_DIMENSION, MAX_DIMENSION)) {
            throw DepartmentMapException.invalid("Map width and height must be between " + MIN_DIMENSION + " and "
                    + MAX_DIMENSION + " m");
        }
        if (!inRange(gridSize, MIN_GRID_SIZE, MAX_GRID_SIZE)) {
            throw DepartmentMapException.invalid("Grid size must be between " + MIN_GRID_SIZE + " and "
                    + MAX_GRID_SIZE + " m");
        }
    }

    private static boolean inRange(final double value, final double min, final double max) {
        return Double.isFinite(value) && value >= min && value <= max;
    }
}
