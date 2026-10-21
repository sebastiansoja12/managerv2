package com.warehouse.departmentmap.domain.model;

import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.departmentmap.domain.enumeration.MapElementType;
import com.warehouse.departmentmap.domain.exception.DepartmentMapException;
import com.warehouse.departmentmap.domain.vo.ElementGeometry;
import com.warehouse.departmentmap.domain.vo.StorageLocationDefinition;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

public class MapElement {

    public static final int MAX_CAPACITY = 99;
    private static final int MAX_LABEL_LENGTH = 120;
    private static final int MAX_CODE_LENGTH = 40;
    private static final Pattern COLOR_PATTERN = Pattern.compile("^#[0-9A-Fa-f]{6}$");
    private static final Pattern CODE_PATTERN = Pattern.compile("^[A-Z0-9][A-Z0-9._/-]*$");

    private final MapElementId elementId;
    private final MapElementType type;
    private final String label;
    private final ElementGeometry geometry;
    private final String color;
    private final String locationCode;
    private final Integer capacity;

    public MapElement(final MapElementId elementId,
                      final MapElementType type,
                      final String label,
                      final ElementGeometry geometry,
                      final String color,
                      final String locationCode,
                      final Integer capacity) {
        if (elementId == null || elementId.value() == null) {
            throw DepartmentMapException.invalid("Element id is required");
        }
        if (type == null) {
            throw DepartmentMapException.invalid("Element type is required");
        }
        if (geometry == null) {
            throw DepartmentMapException.invalid("Element geometry is required");
        }
        this.elementId = elementId;
        this.type = type;
        this.label = normalizeLabel(label);
        this.geometry = geometry;
        this.color = normalizeColor(color);
        if (type == MapElementType.STORAGE_LOCATION) {
            this.locationCode = normalizeCode(locationCode);
            this.capacity = validateCapacity(capacity);
        } else {
            this.locationCode = null;
            this.capacity = null;
        }
    }

    public boolean isStorageLocation() {
        return type == MapElementType.STORAGE_LOCATION;
    }

    public Optional<StorageLocationDefinition> storageLocationDefinition() {
        return isStorageLocation()
                ? Optional.of(new StorageLocationDefinition(elementId, locationCode, capacity))
                : Optional.empty();
    }

    public MapElementId getElementId() {
        return elementId;
    }

    public MapElementType getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public ElementGeometry getGeometry() {
        return geometry;
    }

    public String getColor() {
        return color;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public static String normalizeCode(final String code) {
        if (code == null || code.isBlank()) {
            throw DepartmentMapException.invalid("Storage location code is required");
        }
        final String normalized = code.trim().toUpperCase(Locale.ROOT);
        if (normalized.length() > MAX_CODE_LENGTH || !CODE_PATTERN.matcher(normalized).matches()) {
            throw DepartmentMapException.invalid("Storage location code " + normalized
                    + " may contain only letters, digits and . _ / - characters (max " + MAX_CODE_LENGTH + ")");
        }
        return normalized;
    }

    private static Integer validateCapacity(final Integer capacity) {
        if (capacity == null || capacity < 1 || capacity > MAX_CAPACITY) {
            throw DepartmentMapException.invalid("Storage location capacity must be between 1 and " + MAX_CAPACITY);
        }
        return capacity;
    }

    private static String normalizeLabel(final String label) {
        if (label == null || label.isBlank()) {
            return null;
        }
        final String normalized = label.trim();
        if (normalized.length() > MAX_LABEL_LENGTH) {
            throw DepartmentMapException.invalid("Element label must not exceed " + MAX_LABEL_LENGTH + " characters");
        }
        return normalized;
    }

    private static String normalizeColor(final String color) {
        if (color == null || color.isBlank()) {
            return null;
        }
        if (!COLOR_PATTERN.matcher(color.trim()).matches()) {
            throw DepartmentMapException.invalid("Element color must use #RRGGBB format");
        }
        return color.trim().toUpperCase(Locale.ROOT);
    }
}
