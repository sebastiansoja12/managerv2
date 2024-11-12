package com.warehouse.terminal.domain.port.secondary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.Terminal;

public interface DeviceRepository<T> {
    Object findById(final DeviceId deviceId);
    T saveOrUpdate(final Terminal terminal);
}
