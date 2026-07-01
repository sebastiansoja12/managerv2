package com.warehouse.terminal.domain.port.secondary;

import java.util.List;
import java.util.Optional;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.Device;

public interface DeviceRepository<T extends Device> {
    T findById(final DeviceId deviceId);
    Optional<T> findByExternalSystemId(final String externalSystemId);
    List<T> findByUserId(final UserId userId);
    void saveOrUpdate(final T t);
    List<T> findAll();
    boolean canHandle(final DeviceType type);
    DeviceId nextDeviceId();
}
