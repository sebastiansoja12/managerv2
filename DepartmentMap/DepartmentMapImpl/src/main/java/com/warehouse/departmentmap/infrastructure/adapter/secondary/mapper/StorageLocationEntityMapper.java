package com.warehouse.departmentmap.infrastructure.adapter.secondary.mapper;

import com.warehouse.departmentmap.domain.model.PalletPlacement;
import com.warehouse.departmentmap.domain.model.StorageLocation;
import com.warehouse.departmentmap.domain.vo.LocationReservation;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.entity.PalletPlacementEntity;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.entity.StorageLocationEntity;

public final class StorageLocationEntityMapper {

    private StorageLocationEntityMapper() {
    }

    public static StorageLocationEntity toEntity(final StorageLocation location) {
        final LocationReservation reservation = location.getReservation().orElse(null);
        final StorageLocationEntity entity = new StorageLocationEntity(location.getLocationId(),
                location.getDepartmentMapId(), location.getCode(), location.getCapacity(),
                reservation == null ? null : reservation.reservedAt(),
                reservation == null ? null : reservation.palletTrackingNumber(),
                reservation == null ? null : reservation.note(),
                reservation == null ? null : reservation.reservedBy(),
                location.getVersion(),
                location.getPallets().stream()
                        .map(placement -> new PalletPlacementEntity(placement.placementId(),
                                location.getLocationId(), location.getOperatorId(),
                                placement.palletTrackingNumber(), placement.placedBy(), placement.placedAt()))
                        .toList());
        entity.assignOperator(location.getOperatorId());
        return entity;
    }

    public static StorageLocation toModel(final StorageLocationEntity entity) {
        final LocationReservation reservation = entity.getReservedAt() == null ? null
                : new LocationReservation(entity.getReservedPalletTrackingNumber(), entity.getReservationNote(),
                entity.getReservedBy(), entity.getReservedAt());
        return new StorageLocation(entity.getLocationId(), entity.getDepartmentMapId(), entity.operatorId(),
                entity.getCode(), entity.getCapacity(), reservation,
                entity.getPallets().stream()
                        .map(placement -> new PalletPlacement(placement.getPlacementId(),
                                placement.getPalletTrackingNumber(), placement.getPlacedBy(),
                                placement.getPlacedAt()))
                        .toList(),
                entity.getVersion());
    }
}
