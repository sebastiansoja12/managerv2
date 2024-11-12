package com.warehouse.terminal.domain.port.primary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.model.TerminalVersion;

public interface TerminalPairPort {
    boolean isConnected(final TerminalId terminalId);
    boolean isValid(final TerminalId terminalId);
    boolean isUserValid(final TerminalId terminalId, final UserId userId);
    boolean isVersionValid(final TerminalId terminalId, final TerminalVersion version);
    boolean updateRequired(final TerminalId terminalId, final TerminalVersion version);

    void pair(final DeviceId deviceId);
    void unpair(final Terminal terminal);
}
