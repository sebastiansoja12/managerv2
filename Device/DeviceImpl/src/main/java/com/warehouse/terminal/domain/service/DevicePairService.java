package com.warehouse.terminal.domain.service;

import java.util.Optional;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.vo.IssuedDevicePairKey;

public interface DevicePairService {
    void unpairDevice(final DeviceId deviceId);
    DevicePair findByDeviceId(final DeviceId deviceId);
    Optional<DevicePair> findByPairKey(final String pairKey);
    Optional<DevicePair> findValidByPairKey(final String pairKey);

    Optional<DevicePair> findExistingByDeviceId(final DeviceId deviceId);

    boolean revokePairing(final DeviceId deviceId);

    IssuedDevicePairKey activateDevice(final Device device);

    DevicePair startOperatorSession(final DevicePair devicePair, final UserId userId);

    void endOperatorSession(final DevicePair devicePair, final UserId userId);

    void markSeen(final DevicePair devicePair);
}
