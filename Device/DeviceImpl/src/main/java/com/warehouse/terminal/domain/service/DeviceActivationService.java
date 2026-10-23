package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.DeviceActivationCode;
import com.warehouse.terminal.domain.model.command.DevicePairingCodeCommand;
import com.warehouse.terminal.domain.vo.ActivationCode;
import com.warehouse.terminal.domain.vo.DeviceActivationCodeIssue;

public interface DeviceActivationService {
    DeviceActivationCodeIssue issue(final DeviceId deviceId);
    DeviceActivationCodeIssue issueForUser(final DevicePairingCodeCommand command);
    DeviceActivationCode consume(final ActivationCode activationCode);
    void discardUnused(final DeviceId deviceId);
}
