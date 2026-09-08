package com.warehouse.logistics.application.port.primary;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.terminal.DeviceInformation;

public interface DeviceValidatorPort {

    void validateDevice(final ProcessId processId, final ProcessType processType, final DeviceInformation deviceInformation);
}
