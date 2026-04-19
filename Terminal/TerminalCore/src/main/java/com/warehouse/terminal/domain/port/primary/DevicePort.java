package com.warehouse.terminal.domain.port.primary;

import java.util.List;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.command.DeviceCreateCommand;
import com.warehouse.terminal.domain.model.command.DeviceSettingsRequest;
import com.warehouse.terminal.domain.model.command.DeviceUpdateCommand;
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

    Device getDevice(final DeviceId deviceId);
}
