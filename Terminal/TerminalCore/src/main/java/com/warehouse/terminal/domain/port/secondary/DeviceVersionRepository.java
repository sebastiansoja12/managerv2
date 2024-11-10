package com.warehouse.terminal.domain.port.secondary;

import com.warehouse.commonassets.identificator.TerminalId;

public interface DeviceVersionRepository {
    boolean updateRequired(final TerminalId terminalId);
}
