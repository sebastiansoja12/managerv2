package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;

public record DeviceStatusUpdateCommand(
        DeviceId deviceId,
        DeviceStatus status
) {
}
