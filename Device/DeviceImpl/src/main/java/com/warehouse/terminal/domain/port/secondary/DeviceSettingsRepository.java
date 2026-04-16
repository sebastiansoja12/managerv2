package com.warehouse.terminal.domain.port.secondary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.DeviceSettings;

public interface DeviceSettingsRepository {
    DeviceSettings getDeviceSettings(final DeviceId deviceId);
    void saveOrUpdate(final DeviceSettings deviceSettings);
}
