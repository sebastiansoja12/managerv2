package com.warehouse.terminal.domain.port.primary;

import java.util.List;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.command.*;
import com.warehouse.terminal.domain.vo.DeviceCreateResult;
import com.warehouse.terminal.domain.vo.DeviceUserRequest;
import com.warehouse.terminal.domain.vo.DeviceVersionRequest;

public interface DevicePort {
    DeviceCreateResult create(final DeviceCreateCommand request);
    void changeUserTo(final DeviceUserRequest request);
    void changeVersionTo(final DeviceVersionRequest request);
    void updateDevice(final DeviceUpdateCommand request);
    void updateSettings(final DeviceSettingsRequest request);
    List<Device> allDevices();
    List<Device> findDevicesByUserId(final UserId userId);

    Device getDevice(final DeviceId deviceId);

    void updateDeviceType(final DeviceTypeUpdateCommand request);

    void updateActiveField(final DeviceActiveUpdateCommand request);

    void updateStatusField(final DeviceStatusUpdateCommand request);

    void updateIdentityField(final DeviceIdentityUpdateCommand request);

    void updateHardwareField(final DeviceHardwareUpdateCommand request);

    void updateSoftwareField(final DeviceSoftwareUpdateCommand request);

    void updateNetworkField(final DeviceNetworkUpdateCommand request);

    void updateSecurityField(final DeviceSecurityUpdateCommand request);

    void updateLocationField(final DeviceLocationUpdateCommand request);

    void updateOwnershipField(final DeviceOwnershipUpdateCommand request);

    void updateVersionField(final DeviceVersionUpdateCommand request);
}
