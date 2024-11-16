package com.warehouse.terminal.domain.vo;

import com.warehouse.commonassets.identificator.DeviceId;

public record DeviceVersionRequest(DeviceId deviceId, String version) {
}
