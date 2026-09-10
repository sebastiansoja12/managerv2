package com.warehouse.logistics.application.port.primary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.logistics.application.port.secondary.ProcessHubServicePort;
import com.warehouse.terminal.DeviceInformation;

public class ProcessInitializerPortImpl implements ProcessInitializerPort {

    private final ProcessHubServicePort processHubServicePort;

    public ProcessInitializerPortImpl(final ProcessHubServicePort processHubServicePort) {
        this.processHubServicePort = processHubServicePort;
    }

    @Override
    public ProcessId initializeProcess(final DeviceInformation deviceInformation, final String request) {
        return this.processHubServicePort.initialize(deviceInformation, request);
    }
}
