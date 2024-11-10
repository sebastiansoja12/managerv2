package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.terminal.domain.model.Terminal;

public interface TerminalService {
    void createTerminal(final Terminal terminal);
    void changeDeviceType(final TerminalId terminalId, final DeviceType deviceType);
    void updateVersion(final TerminalId terminalId, final String version);
    Terminal findByTerminalId(final TerminalId terminalId);
}
