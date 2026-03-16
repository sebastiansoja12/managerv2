package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.vo.DeviceSoftware;

public record DeviceSoftwareUpdateCommand(
        DeviceId deviceId,
        DeviceSoftware software
) {
}
