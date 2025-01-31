package com.warehouse.terminal.domain.port.secondary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.vo.DevicePairId;

public interface DevicePairRepository {
    void pair(final DevicePair devicePair);
    void unpair(final Terminal terminal, final DevicePairId devicePairId);
    DevicePairId findPairIdByDeviceId(final DeviceId deviceId);
    DevicePair findDevicePairByDeviceId(final DeviceId deviceId);
    void update(final DevicePair devicePair);
    Optional<DevicePair> findByDeviceId(final DeviceId deviceId);
    void save(final DevicePair devicePair);
}
