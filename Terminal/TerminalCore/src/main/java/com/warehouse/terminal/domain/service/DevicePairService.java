package com.warehouse.terminal.domain.service;

import java.util.Optional;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DevicePair;

public interface DevicePairService {
    DevicePair pairDevice(final Device device);
    void unpairDevice(final DeviceId deviceId);
    DevicePair findByDeviceId(final DeviceId deviceId);
    Optional<DevicePair> findByPairKey(final String pairKey);
    Optional<DevicePair> findValidByPairKey(final String pairKey);
}
