package com.warehouse.terminal.domain.service;

import java.util.List;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DeviceSettings;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.model.command.DeviceUpdateCommand;
import com.warehouse.terminal.domain.vo.*;

public interface DeviceGenericService {
    void create(final Device device);
    void assignUser(final DeviceId deviceId, final UserId userId);
    void updateVersion(final DeviceId deviceId, final String version);
    void updateDevice(final DeviceUpdateCommand request);
    void updateDeviceType(final DeviceId deviceId, final DeviceType deviceType);
    void updateActive(final DeviceId deviceId, final Boolean active);
    void updateStatus(final DeviceId deviceId, final DeviceStatus status);
    void updateIdentity(final DeviceId deviceId, final DeviceIdentity identity);
    void updateHardware(final DeviceId deviceId, final DeviceHardware hardware);
    void updateSoftware(final DeviceId deviceId, final DeviceSoftware software);
    void updateNetwork(final DeviceId deviceId, final DeviceNetwork network);
    void updateSecurity(final DeviceId deviceId, final SecurityProfile security);
    void updateLocation(final DeviceId deviceId, final DeviceLocation location);
    void updateOwnership(final DeviceId deviceId, final OwnershipProfile ownership);
    void updateVersionField(final DeviceId deviceId, final String version);
    Device findByDeviceId(final DeviceId deviceId);
    List<Device> findAll();

    void updateSettings(final DeviceId deviceId, final DeviceSettings deviceSettings);

    DeviceId nextDeviceId(final DeviceType deviceType);
}
