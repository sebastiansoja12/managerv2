package com.warehouse.terminal.domain.port.secondary;

import java.util.List;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.device.Device;

public interface DeviceRepository<T extends Device> {
    T findById(final DeviceId deviceId);
    void saveOrUpdate(final T t);
    List findAll();
    boolean canHandle(final DeviceType type);
    DeviceId nextDeviceId();
}
