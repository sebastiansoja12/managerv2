package com.warehouse.terminal.domain.model.command;

import java.time.Instant;

import com.warehouse.commonassets.identificator.DeviceId;

public record DeviceUpdatedAtUpdateCommand(
        DeviceId deviceId,
        Instant updatedAt
) {
}
