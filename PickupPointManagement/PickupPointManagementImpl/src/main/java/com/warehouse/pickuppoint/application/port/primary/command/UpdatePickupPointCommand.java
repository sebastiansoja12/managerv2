package com.warehouse.pickuppoint.application.port.primary.command;

import com.warehouse.commonassets.identificator.PickupPointId;

import java.util.Objects;

public record UpdatePickupPointCommand(
        PickupPointId pickupPointId,
        PickupPointConfigurationCommand configuration) {

    public UpdatePickupPointCommand {
        Objects.requireNonNull(pickupPointId, "Pickup point ID cannot be null");
        Objects.requireNonNull(configuration, "Pickup point configuration cannot be null");
    }
}
