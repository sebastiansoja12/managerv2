package com.warehouse.terminal.domain.vo;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;

public record DevicePairResponse(UserId userId, DeviceId deviceId, DevicePairId devicePairId,
                                 String pairStatus, String pairKey, Boolean userValid, Boolean deviceValid,
                                 Boolean deviceUpToDate) {
}
