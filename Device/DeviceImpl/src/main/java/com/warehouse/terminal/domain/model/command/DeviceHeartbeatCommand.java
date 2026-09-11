package com.warehouse.terminal.domain.model.command;

import com.warehouse.terminal.domain.vo.DevicePairKey;

public record DeviceHeartbeatCommand(DevicePairKey pairKey,
                                     String version,
                                     String manufacturer,
                                     String model,
                                     String osName,
                                     String osVersion) {
}
