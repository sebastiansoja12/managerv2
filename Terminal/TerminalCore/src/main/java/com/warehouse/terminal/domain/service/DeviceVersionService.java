package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.DeviceVersion;

public interface DeviceVersionService {
    DeviceVersion findByDeviceId(final DeviceId deviceId);
    void saveOrUpdateDeviceVersion(final DeviceId deviceId, final String version);
}
