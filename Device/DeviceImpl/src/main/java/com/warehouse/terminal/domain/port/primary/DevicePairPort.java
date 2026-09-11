package com.warehouse.terminal.domain.port.primary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.domain.model.DeviceVersion;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.model.command.DeviceActivationCommand;
import com.warehouse.terminal.domain.model.command.DeviceHeartbeatCommand;
import com.warehouse.terminal.domain.model.command.DevicePairingCodeCommand;
import com.warehouse.terminal.domain.model.command.DeviceOperatorSessionCommand;
import com.warehouse.terminal.domain.vo.DeviceActivationCodeIssue;
import com.warehouse.terminal.domain.vo.DeviceActivationResult;
import com.warehouse.terminal.domain.vo.DeviceHeartbeatResult;
import com.warehouse.terminal.domain.vo.DeviceOperatorSessionResult;

public interface DevicePairPort {
    boolean isPaired(final DeviceId deviceId);
    boolean isValid(final DeviceId deviceId);
    boolean isPairKeyValid(final String pairKey);
    boolean isUserValid(final DeviceId deviceId, final Username username);
    boolean isVersionValid(final DeviceId deviceId, final DeviceVersion version);
    boolean updateRequired(final DeviceId deviceId, final DeviceVersion version);

    void unpair(final Terminal terminal);

    DeviceActivationCodeIssue issueActivationCode(final DeviceId deviceId);
    DeviceActivationResult activate(final DeviceActivationCommand command);
    DeviceActivationCodeIssue issuePairingCode(final DevicePairingCodeCommand command);
    void revoke(final DeviceId deviceId);

    DeviceOperatorSessionResult startOperatorSession(final DeviceOperatorSessionCommand command);
    void endOperatorSession(final DeviceOperatorSessionCommand command);
    DeviceHeartbeatResult heartbeat(final DeviceHeartbeatCommand command);
}
