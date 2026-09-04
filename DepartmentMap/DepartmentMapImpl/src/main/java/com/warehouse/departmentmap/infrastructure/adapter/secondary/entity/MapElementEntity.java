package com.warehouse.departmentmap.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.departmentmap.domain.enumeration.MapElementType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity(name = "departmentmap.MapElementEntity")
@Table(name = "department_map_elements")
public class MapElementEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "element_id", nullable = false))
    private MapElementId elementId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "map_id", nullable = false,
            insertable = false, updatable = false))
    private DepartmentMapId departmentMapId;

    @Enumerated(EnumType.STRING)
    @Column(name = "element_type", nullable = false, length = 32)
    private MapElementType type;

    @Column(name = "label", length = 120)
    private String label;

    @Column(name = "pos_x", nullable = false)
    private double x;

    @Column(name = "pos_y", nullable = false)
    private double y;

    @Column(name = "width", nullable = false)
    private double width;

    @Column(name = "height", nullable = false)
    private double height;

    @Column(name = "rotation", nullable = false)
    private double rotation;

    @Column(name = "color", length = 7)
    private String color;

    @Column(name = "location_code", length = 40)
    private String locationCode;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    protected MapElementEntity() {
    }

    public MapElementEntity(final MapElementId elementId,
                            final DepartmentMapId departmentMapId,
                            final MapElementType type,
                            final String label,
                            final double x,
                            final double y,
                            final double width,
                            final double height,
                            final double rotation,
                            final String color,
                            final String locationCode,
                            final Integer capacity,
                            final int sortOrder) {
        this.elementId = elementId;
        this.departmentMapId = departmentMapId;
        this.type = type;
        this.label = label;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.color = color;
        this.locationCode = locationCode;
        this.capacity = capacity;
        this.sortOrder = sortOrder;
    }

    public MapElementId getElementId() {
        return elementId;
    }

    public DepartmentMapId getDepartmentMapId() {
        return departmentMapId;
    }

    public MapElementType getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getRotation() {
        return rotation;
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

    public int getSortOrder() {
        return sortOrder;
    }
}
