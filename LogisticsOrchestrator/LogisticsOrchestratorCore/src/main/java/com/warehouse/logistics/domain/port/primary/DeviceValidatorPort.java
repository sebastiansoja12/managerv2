package com.warehouse.logistics.domain.port.primary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.terminal.DeviceInformation;

public interface DeviceValidatorPort {
    void validateDevice(final ProcessId processId,
                        final DeviceInformation deviceInformation);
}
