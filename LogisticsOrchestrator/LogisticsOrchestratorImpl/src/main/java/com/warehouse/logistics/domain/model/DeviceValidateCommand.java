package com.warehouse.logistics.domain.model;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.terminal.DeviceInformation;

public class DeviceValidateCommand {

    private final ProcessId processId;
    private final ProcessType processType;
    private final DeviceInformation deviceInformation;

    public DeviceValidateCommand(final ProcessId processId,
                                 final ProcessType processType,
                                 final DeviceInformation deviceInformation) {
        this.processId = processId;
        this.processType = processType;
        this.deviceInformation = deviceInformation;
    }

    public ProcessId getProcessId() {
        return processId;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }
}
