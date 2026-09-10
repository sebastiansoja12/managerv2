package com.warehouse.departmentmap.infrastructure.adapter.secondary.mapper;

import com.warehouse.departmentmap.domain.model.PalletMovement;
import com.warehouse.departmentmap.domain.vo.LocationReference;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.entity.PalletMovementEntity;

public final class PalletMovementEntityMapper {

    private PalletMovementEntityMapper() {
    }

    public static PalletMovementEntity toEntity(final PalletMovement movement) {
        final LocationReference from = movement.fromLocation();
        final LocationReference to = movement.toLocation();
        return new PalletMovementEntity(movement.movementId(), movement.palletTrackingNumber(),
                movement.movementType(), movement.departmentMapId(),
                from == null ? null : from.locationId(), from == null ? null : from.code(),
                to == null ? null : to.locationId(), to == null ? null : to.code(),
                movement.userId(), movement.occurredAt());
    }

    public static PalletMovement toModel(final PalletMovementEntity entity) {
        return new PalletMovement(entity.getMovementId(), entity.getPalletTrackingNumber(),
                entity.getMovementType(), entity.getDepartmentMapId(),
                entity.getFromLocationCode() == null ? null
                        : new LocationReference(entity.getFromLocationId(), entity.getFromLocationCode()),
                entity.getToLocationCode() == null ? null
                        : new LocationReference(entity.getToLocationId(), entity.getToLocationCode()),
                entity.getUserId(), entity.getOccurredAt());
    }
}
