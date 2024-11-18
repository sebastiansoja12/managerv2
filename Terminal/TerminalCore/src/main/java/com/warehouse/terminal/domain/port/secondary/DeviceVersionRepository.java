package com.warehouse.terminal.domain.port.secondary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.DeviceVersion;

public interface DeviceVersionRepository {
    boolean updateRequired(final DeviceId deviceId);
    DeviceVersion find(final DeviceId deviceId);
    void saveOrUpdate(final DeviceVersion deviceVersion);
}
