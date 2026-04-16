package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.vo.DeviceNetwork;

public record DeviceNetworkUpdateCommand(
        DeviceId deviceId,
        DeviceNetwork network
) {
}
