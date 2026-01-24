package com.warehouse.terminal.domain.model.device;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.enumeration.ExecutionSourceType;
import com.warehouse.terminal.domain.model.DeviceHandler;
import com.warehouse.terminal.domain.model.ExecutionSourceResolver;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.vo.*;

public class Mobile extends Device implements DeviceHandler, ExecutionSourceResolver {
    public Mobile(
            final DeviceId deviceId,
            final DeviceStatus status,
            final IdentityInfo identity,
            final HardwareProfile hardware,
            final SoftwareProfile software,
            final NetworkProfile network,
            final LocationProfile location,
            final OwnershipProfile ownership
    ) {
        super(deviceId, ExternalId.generateId(), DeviceType.MOBILE, status, identity, hardware, software, network, SecurityProfile.empty(), location, ownership);
    }

    @Override
    public boolean canHandle(final DeviceType deviceType) {
        return DeviceType.MOBILE.equals(deviceType);
    }

    @Override
    public ExecutionSourceType getExecutionSourceType() {
        return ExecutionSourceType.DEVICE;
    }
}
