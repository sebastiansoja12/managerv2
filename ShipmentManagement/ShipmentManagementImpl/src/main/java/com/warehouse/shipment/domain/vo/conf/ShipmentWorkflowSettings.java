package com.warehouse.shipment.domain.vo.conf;

import com.warehouse.commonassets.enumeration.ShipmentStatus;

import java.util.Objects;

public record ShipmentWorkflowSettings(
        ShipmentStatus defaultStatus,
        ShipmentServiceLevel defaultServiceLevel,
        boolean autoAssignCourier,
        boolean autoCloseDelivered,
        boolean generateTrackingNumber,
        int cancellationWindowMinutes,
        String pickupCutoffTime
) {

    public ShipmentWorkflowSettings {
        defaultStatus = Objects.requireNonNullElse(defaultStatus, ShipmentStatus.CREATED);
        defaultServiceLevel = Objects.requireNonNullElse(defaultServiceLevel, ShipmentServiceLevel.STANDARD);
        pickupCutoffTime = pickupCutoffTime == null || pickupCutoffTime.isBlank() ? "16:00" : pickupCutoffTime;
    }

    public static ShipmentWorkflowSettings defaults() {
        return new ShipmentWorkflowSettings(
                ShipmentStatus.CREATED,
                ShipmentServiceLevel.STANDARD,
                false,
                true,
                false,
                30,
                "16:00"
        );
    }
}
