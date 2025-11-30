package com.warehouse.terminal.domain.port.primary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.model.request.DeviceSettingsRequest;
import com.warehouse.terminal.domain.model.request.TerminalAddRequest;
import com.warehouse.terminal.domain.vo.DeviceTypeRequest;
import com.warehouse.terminal.domain.vo.DeviceUserRequest;
import com.warehouse.terminal.domain.vo.DeviceVersionRequest;

import java.util.List;

public interface TerminalPort {
    void create(final TerminalAddRequest request);
    void changeDeviceTypeTo(final DeviceTypeRequest request);
    void changeUserTo(final DeviceUserRequest request);
    void changeVersionTo(final DeviceVersionRequest request);
    void updateSettings(final DeviceSettingsRequest request);
    List<Terminal> allDevices();

    Terminal getDevice(final DeviceId deviceId);
}
