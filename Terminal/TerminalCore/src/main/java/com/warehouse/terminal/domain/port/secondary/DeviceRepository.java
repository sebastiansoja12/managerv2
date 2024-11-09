package com.warehouse.terminal.domain.port.secondary;

import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.terminal.domain.model.Terminal;

public interface DeviceRepository<T> {
    Object findById(final TerminalId id);
    T save(final Terminal terminal);
}
