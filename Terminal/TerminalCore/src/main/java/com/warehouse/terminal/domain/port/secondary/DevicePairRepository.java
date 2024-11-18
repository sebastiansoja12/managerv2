package com.warehouse.terminal.domain.port.secondary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.vo.DevicePairId;

public interface DevicePairRepository {
    void pair(final Terminal terminal, final DevicePairId devicePairId);
    void unpair(final Terminal terminal, final DevicePairId devicePairId);
    DevicePairId findPairIdByDeviceId(final DeviceId deviceId);
    DevicePair findDevicePairByDeviceId(final DeviceId deviceId);
    void update(final DevicePair devicePair);
}
