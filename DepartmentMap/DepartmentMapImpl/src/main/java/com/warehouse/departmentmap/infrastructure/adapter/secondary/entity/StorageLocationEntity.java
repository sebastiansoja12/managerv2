package com.warehouse.departmentmap.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.UserId;
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

@Entity(name = "departmentmap.StorageLocationEntity")
@Table(name = "department_map_locations")
public class StorageLocationEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "location_id", nullable = false))
    private MapElementId locationId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "map_id", nullable = false))
    private DepartmentMapId departmentMapId;

    @Column(name = "location_code", nullable = false, length = 40)
    private String code;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "reserved_at")
    private Instant reservedAt;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "reserved_pallet_tracking_number", length = 64))
    private PalletTrackingNumber reservedPalletTrackingNumber;

    @Column(name = "reservation_note")
    private String reservationNote;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "reserved_by_user_id"))
    private UserId reservedBy;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", referencedColumnName = "location_id", nullable = false)
    @OrderBy("placedAt ASC")
    private List<PalletPlacementEntity> pallets = new ArrayList<>();

    protected StorageLocationEntity() {
    }

    public StorageLocationEntity(final MapElementId locationId,
                                 final DepartmentMapId departmentMapId,
                                 final String code,
                                 final int capacity,
                                 final Instant reservedAt,
                                 final PalletTrackingNumber reservedPalletTrackingNumber,
                                 final String reservationNote,
                                 final UserId reservedBy,
                                 final Long version,
                                 final List<PalletPlacementEntity> pallets) {
        this.locationId = locationId;
        this.departmentMapId = departmentMapId;
        this.code = code;
        this.capacity = capacity;
        this.reservedAt = reservedAt;
        this.reservedPalletTrackingNumber = reservedPalletTrackingNumber;
        this.reservationNote = reservationNote;
        this.reservedBy = reservedBy;
        this.version = version;
        this.pallets = new ArrayList<>(pallets);
    }

    public MapElementId getLocationId() {
        return locationId;
    }

    public DepartmentMapId getDepartmentMapId() {
        return departmentMapId;
    }

    public String getCode() {
        return code;
    }

    public int getCapacity() {
        return capacity;
    }

    public Instant getReservedAt() {
        return reservedAt;
    }

    public PalletTrackingNumber getReservedPalletTrackingNumber() {
        return reservedPalletTrackingNumber;
    }

    public String getReservationNote() {
        return reservationNote;
    }

    public UserId getReservedBy() {
        return reservedBy;
    }

    public Long getVersion() {
        return version;
    }

    public List<PalletPlacementEntity> getPallets() {
        return pallets;
    }
}
