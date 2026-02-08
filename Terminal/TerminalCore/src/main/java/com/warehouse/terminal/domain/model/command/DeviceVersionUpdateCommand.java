package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.identificator.DeviceId;

public record DeviceVersionUpdateCommand(
        DeviceId deviceId,
        String version
) {
}
