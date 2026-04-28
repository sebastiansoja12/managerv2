package com.warehouse.logistics.domain.port.secondary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.terminal.DeviceInformation;

public interface DeviceAgentServicePort {
    void validateDevice(final ProcessId processId, final DeviceInformation deviceInformation);
    void updateDevice(final DeviceInformation deviceInformation);
}
