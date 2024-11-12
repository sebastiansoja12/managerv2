package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.model.Terminal;

public interface DevicePairService {
    void pairDevice(final Terminal terminal);
    void unpairDevice(final DeviceId deviceId);
    DevicePair findByDeviceId(final DeviceId deviceId);
}
