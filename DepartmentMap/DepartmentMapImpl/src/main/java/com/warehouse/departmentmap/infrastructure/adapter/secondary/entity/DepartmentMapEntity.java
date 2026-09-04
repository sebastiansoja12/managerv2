package com.warehouse.departmentmap.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "departmentmap.DepartmentMapEntity")
@Table(name = "department_maps")
public class DepartmentMapEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "map_id", nullable = false))
    private DepartmentMapId departmentMapId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "department_id", nullable = false))
    private DepartmentId departmentId;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "width", nullable = false)
    private double width;

    @Column(name = "height", nullable = false)
    private double height;

    @Column(name = "grid_size", nullable = false)
    private double gridSize;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "map_id", referencedColumnName = "map_id", nullable = false)
    @OrderBy("sortOrder ASC")
    private List<MapElementEntity> elements = new ArrayList<>();

    protected DepartmentMapEntity() {
    }

    public DepartmentMapEntity(final DepartmentMapId departmentMapId,
                               final DepartmentId departmentId,
                               final String name,
                               final double width,
                               final double height,
                               final double gridSize,
                               final Long version,
                               final Instant createdAt,
                               final Instant updatedAt,
                               final List<MapElementEntity> elements) {
        this.departmentMapId = departmentMapId;
        this.departmentId = departmentId;
        this.name = name;
        this.width = width;
        this.height = height;
        this.gridSize = gridSize;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.elements = new ArrayList<>(elements);
    }

    public DepartmentMapId getDepartmentMapId() {
        return departmentMapId;
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

    public Long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public List<MapElementEntity> getElements() {
        return elements;
    }
}
