package com.warehouse.logistics.domain.port.primary;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.jaxb.TerminalRequest;

public interface DeviceValidatorPort {
    void validateDevice(final ProcessId processId, final TerminalRequest terminalRequest);

    void validateDevice(final ProcessId processId, final ProcessType processType, final DeviceInformation deviceInformation);
}
