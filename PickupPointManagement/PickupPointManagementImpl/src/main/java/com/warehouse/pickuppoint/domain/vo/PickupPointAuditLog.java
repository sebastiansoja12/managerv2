package com.warehouse.pickuppoint.domain.vo;

import com.warehouse.commonassets.helper.FieldDifference;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointAuditLogType;

import java.util.List;
import java.util.Objects;

public record PickupPointAuditLog(PickupPointId pickupPointId,
                                  PickupPointAuditLogType type,
                                  List<FieldDifference> changes) {

    public PickupPointAuditLog {
        Objects.requireNonNull(pickupPointId, "Pickup point ID cannot be null");
        Objects.requireNonNull(type, "Pickup point audit log type cannot be null");
        changes = List.copyOf(Objects.requireNonNull(changes, "Pickup point changes cannot be null"));
    }
}
