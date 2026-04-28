package com.warehouse.logistics.domain.port.primary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.terminal.jaxb.TerminalRequest;

public interface ProcessInitializerPort {
    ProcessId initializeProcess(final TerminalRequest request);
}
