package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DeliveryAreaId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.organisationstructure.deliveryarea.domain.enumeration.DeliveryAreaSource;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "delivery_area")
@Entity(name = "deliveryarea.DeliveryAreaEntity")
public class DeliveryAreaEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private DeliveryAreaId deliveryAreaId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "department_id", nullable = false, updatable = false))
    private DepartmentId departmentId;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false, length = 20)
    private DeliveryAreaSource source;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "delivery_area_point", joinColumns = @JoinColumn(name = "delivery_area_id"))
    @OrderColumn(name = "point_index")
    private List<DeliveryAreaPointEmbeddable> points = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected DeliveryAreaEntity() {
    }

    public DeliveryAreaEntity(final DeliveryAreaId deliveryAreaId,
                              final DepartmentId departmentId,
                              final String name,
                              final DeliveryAreaSource source,
                              final List<DeliveryAreaPointEmbeddable> points,
                              final Instant createdAt,
                              final Instant updatedAt) {
        this.deliveryAreaId = deliveryAreaId;
        this.departmentId = departmentId;
        this.name = name;
        this.source = source;
        this.points = new ArrayList<>(points);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
