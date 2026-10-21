package com.warehouse.departmentmap.domain.model;

import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.commonassets.identificator.PalletMovementId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.departmentmap.domain.enumeration.PalletMovementType;
import com.warehouse.departmentmap.domain.vo.LocationReference;

import java.time.Instant;

public record PalletMovement(PalletMovementId movementId,
                             PalletTrackingNumber palletTrackingNumber,
                             PalletMovementType movementType,
                             DepartmentMapId departmentMapId,
                             LocationReference fromLocation,
                             LocationReference toLocation,
                             UserId userId,
                             Instant occurredAt) {

    public static PalletMovement placed(final PalletTrackingNumber palletTrackingNumber,
                                        final StorageLocation location,
                                        final UserId userId,
                                        final Instant now) {
        return new PalletMovement(PalletMovementId.generate(), palletTrackingNumber, PalletMovementType.PLACED,
                location.getDepartmentMapId(), null, location.reference(), userId, now);
    }

    public static PalletMovement moved(final PalletTrackingNumber palletTrackingNumber,
                                       final StorageLocation source,
                                       final StorageLocation target,
                                       final UserId userId,
                                       final Instant now) {
        return new PalletMovement(PalletMovementId.generate(), palletTrackingNumber, PalletMovementType.MOVED,
                target.getDepartmentMapId(), source.reference(), target.reference(), userId, now);
    }

    public static PalletMovement removed(final PalletTrackingNumber palletTrackingNumber,
                                         final StorageLocation location,
                                         final UserId userId,
                                         final Instant now) {
        return new PalletMovement(PalletMovementId.generate(), palletTrackingNumber, PalletMovementType.REMOVED,
                location.getDepartmentMapId(), location.reference(), null, userId, now);
    }

    public static PalletMovement reserved(final PalletTrackingNumber palletTrackingNumber,
                                          final StorageLocation location,
                                          final UserId userId,
                                          final Instant now) {
        return new PalletMovement(PalletMovementId.generate(), palletTrackingNumber, PalletMovementType.RESERVED,
                location.getDepartmentMapId(), null, location.reference(), userId, now);
    }

    public static PalletMovement reservationCancelled(final PalletTrackingNumber palletTrackingNumber,
                                                      final StorageLocation location,
                                                      final UserId userId,
                                                      final Instant now) {
        return new PalletMovement(PalletMovementId.generate(), palletTrackingNumber,
                PalletMovementType.RESERVATION_CANCELLED, location.getDepartmentMapId(), location.reference(), null,
                userId, now);
    }
}
