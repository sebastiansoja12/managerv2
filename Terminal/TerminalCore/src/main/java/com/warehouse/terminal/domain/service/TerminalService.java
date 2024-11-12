package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.Terminal;

public interface TerminalService {
    void createTerminal(final Terminal terminal);
    void changeDeviceType(final DeviceId deviceId, final DeviceType deviceType);
    void assignUser(final DeviceId deviceId, final UserId userId);
    void updateVersion(final DeviceId deviceId, final String version);
    Terminal findByDeviceId(final DeviceId deviceId);
}
