package com.warehouse.terminal.domain.port.primary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.DeviceVersion;
import com.warehouse.terminal.domain.model.Terminal;

public interface TerminalPairPort {
    boolean isConnected(final DeviceId deviceId);
    boolean isValid(final DeviceId deviceId);
    boolean isUserValid(final DeviceId deviceId, final UserId userId);
    boolean isVersionValid(final DeviceId deviceId, final DeviceVersion version);
    boolean updateRequired(final DeviceId deviceId, final DeviceVersion version);

    void pair(final DeviceId deviceId);
    void unpair(final Terminal terminal);
}
