package com.warehouse.terminal.domain.service;

import java.util.List;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.command.DeviceSettingsRequest;
import com.warehouse.terminal.domain.model.device.Device;
import com.warehouse.terminal.domain.model.device.Terminal;

public interface DeviceGenericService {
    void create(final Device device);
    void changeDeviceType(final DeviceId deviceId, final DeviceType deviceType);
    void assignUser(final DeviceId deviceId, final UserId userId);
    void updateVersion(final DeviceId deviceId, final String version);
    Terminal findByDeviceId(final DeviceId deviceId);
    List<Terminal> findAll();

    void updateSettings(final DeviceSettingsRequest request);

    DeviceId nextDeviceId(final DeviceType deviceType);
}
