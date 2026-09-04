package com.warehouse.departmentmap.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.commonassets.identificator.PalletMovementId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.departmentmap.domain.enumeration.PalletMovementType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity(name = "departmentmap.PalletMovementEntity")
@Table(name = "department_map_pallet_movements")
public class PalletMovementEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "movement_id", nullable = false))
    private PalletMovementId movementId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "pallet_tracking_number", nullable = false,
            length = 64))
    private PalletTrackingNumber palletTrackingNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 32)
    private PalletMovementType movementType;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "map_id", nullable = false))
    private DepartmentMapId departmentMapId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "from_location_id"))
    private MapElementId fromLocationId;

    @Column(name = "from_location_code", length = 40)
    private String fromLocationCode;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "to_location_id"))
    private MapElementId toLocationId;

    @Column(name = "to_location_code", length = 40)
    private String toLocationCode;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserId userId;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    protected PalletMovementEntity() {
    }

    public PalletMovementEntity(final PalletMovementId movementId,
                                final PalletTrackingNumber palletTrackingNumber,
                                final PalletMovementType movementType,
                                final DepartmentMapId departmentMapId,
                                final MapElementId fromLocationId,
                                final String fromLocationCode,
                                final MapElementId toLocationId,
                                final String toLocationCode,
                                final UserId userId,
                                final Instant occurredAt) {
        this.movementId = movementId;
        this.palletTrackingNumber = palletTrackingNumber;
        this.movementType = movementType;
        this.departmentMapId = departmentMapId;
        this.fromLocationId = fromLocationId;
        this.fromLocationCode = fromLocationCode;
        this.toLocationId = toLocationId;
        this.toLocationCode = toLocationCode;
        this.userId = userId;
        this.occurredAt = occurredAt;
    }

    public PalletMovementId getMovementId() {
        return movementId;
    }

    public PalletTrackingNumber getPalletTrackingNumber() {
        return palletTrackingNumber;
    }

    public PalletMovementType getMovementType() {
        return movementType;
    }

    public DepartmentMapId getDepartmentMapId() {
        return departmentMapId;
    }

    public MapElementId getFromLocationId() {
        return fromLocationId;
    }

    public String getFromLocationCode() {
        return fromLocationCode;
    }

    public MapElementId getToLocationId() {
        return toLocationId;
    }

    public String getToLocationCode() {
        return toLocationCode;
    }

    public UserId getUserId() {
        return userId;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}
