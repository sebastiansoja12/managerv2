package com.warehouse.supplier.domain.port.secondary;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.DeviceId;

public interface DeviceServicePort {
    Result<Void, String> validateDevice(final DeviceId deviceId);
}
