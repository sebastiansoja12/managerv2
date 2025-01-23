package com.warehouse.delivery.domain.port.primary;

import com.warehouse.terminal.request.TerminalRequest;

public interface TerminalRequestLoggerPort {
    void logRequest(final TerminalRequest terminalRequest);

    void logDeviceId(final TerminalRequest terminalRequest);

    void logVersion(final TerminalRequest terminalRequest);

    void logDeviceInformation(final TerminalRequest terminalRequest);
}
