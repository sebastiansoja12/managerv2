package com.warehouse.pickuppoint.application.port.primary.command;

import com.warehouse.pickuppoint.domain.vo.PickupPointCode;

import java.util.Objects;

public record CreatePickupPointCommand(
        PickupPointCode code,
        PickupPointConfigurationCommand configuration) {

    public CreatePickupPointCommand {
        Objects.requireNonNull(code, "Pickup point code cannot be null");
        Objects.requireNonNull(configuration, "Pickup point configuration cannot be null");
    }
}
