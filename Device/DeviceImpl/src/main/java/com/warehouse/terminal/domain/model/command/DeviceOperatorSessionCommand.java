package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.vo.DevicePairKey;

public record DeviceOperatorSessionCommand(DevicePairKey pairKey, UserId userId) {
}
