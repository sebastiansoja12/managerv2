package com.warehouse.deliveryreturn.domain.port.primary;

import com.warehouse.terminal.request.TerminalRequest;

public interface TerminalRequestLoggerPort {
    void logDeliveryMissedTerminalRequest(final TerminalRequest terminalRequest);

    void logTerminalId(final TerminalRequest terminalRequest);

    void logVersion(final TerminalRequest terminalRequest);
}
