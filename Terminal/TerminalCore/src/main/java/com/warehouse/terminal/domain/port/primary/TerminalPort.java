package com.warehouse.terminal.domain.port.primary;

import java.util.List;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.model.request.TerminalAddRequest;

public interface TerminalPort {
    void update(final DeviceId deviceId);
    void create(final TerminalAddRequest request);
    List<Terminal> allDevices();
}
