package com.warehouse.organisationstructure.deliveryarea.domain.model;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.deliveryarea.domain.enumeration.DeliveryAreaSource;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaBoundary;
import com.warehouse.commonassets.identificator.DeliveryAreaId;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaName;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.GeoPoint;

import java.time.Instant;
import java.util.Objects;

public class DeliveryArea {

    private DeliveryAreaId deliveryAreaId;
    private DepartmentId departmentId;
    private DeliveryAreaName name;
    private DeliveryAreaBoundary boundary;
    private DeliveryAreaSource source;
    private Instant createdAt;
    private Instant updatedAt;

    public DeliveryArea(final DepartmentId departmentId,
                        final DeliveryAreaName name,
                        final DeliveryAreaBoundary boundary,
                        final DeliveryAreaSource source,
                        final Instant createdAt) {
        this.deliveryAreaId = DeliveryAreaId.generate();
        this.departmentId = Objects.requireNonNull(departmentId, "Department id must not be null");
        this.name = Objects.requireNonNull(name, "Name must not be null");
        this.boundary = Objects.requireNonNull(boundary, "Boundary must not be null");
        this.source = Objects.requireNonNull(source, "Source must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at must not be null");
        this.updatedAt = createdAt;
    }

    public static DeliveryArea rehydrate(final DeliveryAreaId deliveryAreaId,
                                         final DepartmentId departmentId,
                                         final DeliveryAreaName name,
                                         final DeliveryAreaBoundary boundary,
                                         final DeliveryAreaSource source,
                                         final Instant createdAt,
                                         final Instant updatedAt) {
        final DeliveryArea deliveryArea = new DeliveryArea(departmentId, name, boundary, source, createdAt);
        deliveryArea.deliveryAreaId = deliveryAreaId;
        deliveryArea.updatedAt = updatedAt;
        return deliveryArea;
    }

    public void update(final DeliveryAreaName name, final DeliveryAreaBoundary boundary, final Instant updatedAt) {
        if (this.name.equals(name) && this.boundary.equals(boundary)) {
            return;
        }
        this.name = Objects.requireNonNull(name, "Name must not be null");
        this.boundary = Objects.requireNonNull(boundary, "Boundary must not be null");
        this.source = DeliveryAreaSource.MANUAL;
        this.updatedAt = updatedAt;
    }

    public boolean covers(final GeoPoint location) {
        return boundary.contains(location);
    }

    public DeliveryAreaId getDeliveryAreaId() {
        return deliveryAreaId;
    }

    public DepartmentId getDepartmentId() {
        return departmentId;
    }

    public DeliveryAreaName getName() {
        return name;
    }

    public DeliveryAreaBoundary getBoundary() {
        return boundary;
    }

    public DeliveryAreaSource getSource() {
        return source;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
