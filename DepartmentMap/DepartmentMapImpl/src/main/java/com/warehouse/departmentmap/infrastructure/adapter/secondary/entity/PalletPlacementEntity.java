package com.warehouse.departmentmap.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PalletPlacementId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.UserId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

@Entity(name = "departmentmap.PalletPlacementEntity")
@Table(name = "department_map_location_pallets", uniqueConstraints = @UniqueConstraint(
        name = "uk_department_map_location_pallets_pallet", columnNames = {"operator_id", "pallet_tracking_number"}))
public class PalletPlacementEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "placement_id", nullable = false))
    private PalletPlacementId placementId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "location_id", nullable = false,
            insertable = false, updatable = false))
    private MapElementId locationId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "operator_id", nullable = false))
    private OperatorId operatorId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "pallet_tracking_number", nullable = false,
            length = 64))
    private PalletTrackingNumber palletTrackingNumber;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "placed_by_user_id"))
    private UserId placedBy;

    @Column(name = "placed_at", nullable = false)
    private Instant placedAt;

    protected PalletPlacementEntity() {
    }

    public PalletPlacementEntity(final PalletPlacementId placementId,
                                 final MapElementId locationId,
                                 final OperatorId operatorId,
                                 final PalletTrackingNumber palletTrackingNumber,
                                 final UserId placedBy,
                                 final Instant placedAt) {
        this.placementId = placementId;
        this.locationId = locationId;
        this.operatorId = operatorId;
        this.palletTrackingNumber = palletTrackingNumber;
        this.placedBy = placedBy;
        this.placedAt = placedAt;
    }

    public PalletPlacementId getPlacementId() {
        return placementId;
    }

    public MapElementId getLocationId() {
        return locationId;
    }

    public OperatorId getOperatorId() {
        return operatorId;
    }

    public PalletTrackingNumber getPalletTrackingNumber() {
        return palletTrackingNumber;
    }

    public UserId getPlacedBy() {
        return placedBy;
    }

    public Instant getPlacedAt() {
        return placedAt;
    }
}
