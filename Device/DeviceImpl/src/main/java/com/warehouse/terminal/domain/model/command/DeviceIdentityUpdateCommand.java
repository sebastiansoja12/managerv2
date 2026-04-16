package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.vo.DeviceIdentity;

public record DeviceIdentityUpdateCommand(
        DeviceId deviceId,
        DeviceIdentity identity
) {
}
