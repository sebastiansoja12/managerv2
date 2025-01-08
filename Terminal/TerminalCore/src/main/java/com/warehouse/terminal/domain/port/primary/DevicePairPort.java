package com.warehouse.terminal.domain.port.primary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.DeviceVersion;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.model.request.DevicePairRequest;
import com.warehouse.terminal.domain.model.response.DevicePairResponse;

public interface DevicePairPort {
    boolean isPaired(final DeviceId deviceId);
    boolean isValid(final DeviceId deviceId);
    boolean isUserValid(final DeviceId deviceId, final UserId userId);
    boolean isVersionValid(final DeviceId deviceId, final DeviceVersion version);
    boolean updateRequired(final DeviceId deviceId, final DeviceVersion version);

    DevicePairResponse pair(final DevicePairRequest request);
    void unpair(final Terminal terminal);
}
