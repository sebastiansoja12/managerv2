package com.warehouse.deliverymissed.domain.port.primary;

import com.warehouse.terminal.request.TerminalRequest;

public interface TerminalRequestLoggerPort {
    void logDeliveryMissedTerminalRequest(final TerminalRequest terminalRequest);
}
