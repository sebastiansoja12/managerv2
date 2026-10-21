package com.warehouse.departmentmap.domain.model;

import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PalletPlacementId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.departmentmap.domain.enumeration.LocationStatus;
import com.warehouse.departmentmap.domain.exception.DepartmentMapException;
import com.warehouse.departmentmap.domain.vo.LocationReference;
import com.warehouse.departmentmap.domain.vo.LocationReservation;
import com.warehouse.departmentmap.domain.vo.StorageLocationDefinition;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StorageLocation {

    private static final int MAX_NOTE_LENGTH = 255;

    private final MapElementId locationId;
    private final DepartmentMapId departmentMapId;
    private final OperatorId operatorId;
    private final List<PalletPlacement> pallets;
    private final Long version;
    private String code;
    private int capacity;
    private LocationReservation reservation;

    public StorageLocation(final MapElementId locationId,
                           final DepartmentMapId departmentMapId,
                           final OperatorId operatorId,
                           final String code,
                           final int capacity,
                           final LocationReservation reservation,
                           final List<PalletPlacement> pallets,
                           final Long version) {
        this.locationId = locationId;
        this.departmentMapId = departmentMapId;
        this.operatorId = operatorId;
        this.code = code;
        this.capacity = capacity;
        this.reservation = reservation;
        this.pallets = new ArrayList<>(pallets);
        this.version = version;
    }

    public static StorageLocation define(final StorageLocationDefinition definition,
                                         final DepartmentMapId departmentMapId,
                                         final OperatorId operatorId) {
        return new StorageLocation(definition.locationId(), departmentMapId, operatorId, definition.code(),
                definition.capacity(), null, List.of(), null);
    }

    public boolean differsFrom(final StorageLocationDefinition definition) {
        return !code.equals(definition.code()) || capacity != definition.capacity();
    }

    public void redefine(final StorageLocationDefinition definition) {
        if (definition.capacity() < pallets.size()) {
            throw DepartmentMapException.conflict("CAPACITY_BELOW_OCCUPANCY", "Location " + code + " holds "
                    + pallets.size() + " pallets, capacity cannot be reduced to " + definition.capacity());
        }
        this.code = definition.code();
        this.capacity = definition.capacity();
    }

    public PalletPlacement place(final PalletTrackingNumber palletTrackingNumber,
                                 final UserId userId,
                                 final Instant now) {
        if (palletTrackingNumber == null) {
            throw DepartmentMapException.invalid("Pallet number is required");
        }
        if (holds(palletTrackingNumber)) {
            throw DepartmentMapException.conflict("PALLET_ALREADY_PLACED",
                    "Pallet " + palletTrackingNumber.value() + " is already placed on location " + code);
        }
        final int freeSlots = freeSlots();
        if (freeSlots <= 0) {
            throw DepartmentMapException.conflict("LOCATION_OCCUPIED", capacity == 1
                    ? "Location " + code + " is already occupied by another pallet"
                    : "Location " + code + " is full (" + capacity + " pallets)");
        }
        final boolean matchesReservation = reservation != null && reservation.isFor(palletTrackingNumber);
        if (reservation != null && !matchesReservation && freeSlots <= 1) {
            throw DepartmentMapException.conflict("LOCATION_RESERVED", "Location " + code + " is reserved"
                    + reservation.reservedPallet().map(pallet -> " for pallet " + pallet.value()).orElse(""));
        }
        final PalletPlacement placement = new PalletPlacement(PalletPlacementId.generate(), palletTrackingNumber,
                userId, now);
        pallets.add(placement);
        if (matchesReservation) {
            reservation = null;
        }
        return placement;
    }

    public PalletPlacement remove(final PalletTrackingNumber palletTrackingNumber) {
        final PalletPlacement placement = findPallet(palletTrackingNumber)
                .orElseThrow(() -> DepartmentMapException.notFound("PALLET_NOT_ON_LOCATION",
                        "Pallet " + palletTrackingNumber.value() + " is not placed on location " + code));
        pallets.remove(placement);
        return placement;
    }

    public LocationReservation reserve(final PalletTrackingNumber palletTrackingNumber,
                                       final String note,
                                       final UserId userId,
                                       final Instant now) {
        if (reservation != null) {
            throw DepartmentMapException.conflict("LOCATION_ALREADY_RESERVED", "Location " + code
                    + " is already reserved");
        }
        if (freeSlots() <= 0) {
            throw DepartmentMapException.conflict("LOCATION_OCCUPIED", "Location " + code
                    + " has no free slot to reserve");
        }
        if (palletTrackingNumber != null && holds(palletTrackingNumber)) {
            throw DepartmentMapException.invalid("Pallet " + palletTrackingNumber.value()
                    + " is already placed on location " + code);
        }
        this.reservation = new LocationReservation(palletTrackingNumber, normalizeNote(note), userId, now);
        return reservation;
    }

    public LocationReservation cancelReservation() {
        if (reservation == null) {
            throw DepartmentMapException.conflict("LOCATION_NOT_RESERVED", "Location " + code + " is not reserved");
        }
        final LocationReservation cancelled = reservation;
        this.reservation = null;
        return cancelled;
    }

    public LocationStatus status() {
        if (pallets.size() >= capacity) {
            return LocationStatus.OCCUPIED;
        }
        if (reservation != null) {
            return LocationStatus.RESERVED;
        }
        return pallets.isEmpty() ? LocationStatus.FREE : LocationStatus.PARTIALLY_OCCUPIED;
    }

    public boolean isEmpty() {
        return pallets.isEmpty();
    }

    public boolean holds(final PalletTrackingNumber palletTrackingNumber) {
        return findPallet(palletTrackingNumber).isPresent();
    }

    public Optional<PalletPlacement> findPallet(final PalletTrackingNumber palletTrackingNumber) {
        return pallets.stream()
                .filter(placement -> placement.palletTrackingNumber().equals(palletTrackingNumber))
                .findFirst();
    }

    public LocationReference reference() {
        return new LocationReference(locationId, code);
    }

    public MapElementId getLocationId() {
        return locationId;
    }

    public DepartmentMapId getDepartmentMapId() {
        return departmentMapId;
    }

    public OperatorId getOperatorId() {
        return operatorId;
    }

    public String getCode() {
        return code;
    }

    public int getCapacity() {
        return capacity;
    }

    public Optional<LocationReservation> getReservation() {
        return Optional.ofNullable(reservation);
    }

    public List<PalletPlacement> getPallets() {
        return List.copyOf(pallets);
    }

    public Long getVersion() {
        return version;
    }

    private int freeSlots() {
        return capacity - pallets.size();
    }

    private static String normalizeNote(final String note) {
        if (note == null || note.isBlank()) {
            return null;
        }
        final String normalized = note.trim();
        if (normalized.length() > MAX_NOTE_LENGTH) {
            throw DepartmentMapException.invalid("Reservation note must not exceed " + MAX_NOTE_LENGTH + " characters");
        }
        return normalized;
    }
}
