package com.warehouse.pickuppoint.application.port.primary.command;

import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;

import java.util.Objects;

public record ChangePickupPointStatusCommand(
        PickupPointId pickupPointId,
        PickupPointStatus targetStatus,
        String reason) {

    public ChangePickupPointStatusCommand {
        Objects.requireNonNull(pickupPointId, "Pickup point ID cannot be null");
        Objects.requireNonNull(targetStatus, "Target pickup point status cannot be null");
    }
}
