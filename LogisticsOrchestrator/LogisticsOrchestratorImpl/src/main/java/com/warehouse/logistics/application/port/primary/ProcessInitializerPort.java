package com.warehouse.logistics.application.port.primary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.terminal.DeviceInformation;

public interface ProcessInitializerPort {

    ProcessId initializeProcess(final DeviceInformation deviceInformation, final String request);
}
