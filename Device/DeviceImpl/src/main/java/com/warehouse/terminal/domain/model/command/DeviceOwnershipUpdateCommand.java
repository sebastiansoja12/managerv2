package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.OwnershipProfile;

public record DeviceOwnershipUpdateCommand(
        DeviceId deviceId,
        OwnershipProfile ownership
) {
}
