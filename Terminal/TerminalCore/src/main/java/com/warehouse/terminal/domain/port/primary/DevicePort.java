package com.warehouse.terminal.domain.port.primary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.model.command.DeviceSettingsRequest;
import com.warehouse.terminal.domain.model.command.DeviceCreateCommand;
import com.warehouse.terminal.domain.vo.DeviceCreateResult;
import com.warehouse.terminal.domain.vo.DeviceTypeChangeCommand;
import com.warehouse.terminal.domain.vo.DeviceUserRequest;
import com.warehouse.terminal.domain.vo.DeviceVersionRequest;

import java.util.List;

public interface DevicePort {
    DeviceCreateResult create(final DeviceCreateCommand request);
    void changeDeviceTypeTo(final DeviceTypeChangeCommand request);
    void changeUserTo(final DeviceUserRequest request);
    void changeVersionTo(final DeviceVersionRequest request);
    void updateSettings(final DeviceSettingsRequest request);
    List<Terminal> allDevices();

    Terminal getDevice(final DeviceId deviceId);
}
