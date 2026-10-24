package com.warehouse.logistics.application.port.secondary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.logistics.domain.vo.ProcessCommunication;
import com.warehouse.terminal.DeviceInformation;

public interface ProcessHubServicePort {
    ProcessId initialize(final DeviceInformation deviceInformation, final String request);
    void registerCommunication(final ProcessCommunication communication);
}
