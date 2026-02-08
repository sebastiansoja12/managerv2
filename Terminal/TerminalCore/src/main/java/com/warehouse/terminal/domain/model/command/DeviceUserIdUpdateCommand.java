package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;

public record DeviceUserIdUpdateCommand(
        DeviceId deviceId,
        UserId userId
) {
}
