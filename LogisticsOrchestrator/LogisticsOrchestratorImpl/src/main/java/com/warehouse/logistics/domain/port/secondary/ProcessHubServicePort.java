package com.warehouse.logistics.domain.port.secondary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.terminal.DeviceInformation;

public interface ProcessHubServicePort {
    ProcessId initialize(final DeviceInformation deviceInformation, final String request);
}
