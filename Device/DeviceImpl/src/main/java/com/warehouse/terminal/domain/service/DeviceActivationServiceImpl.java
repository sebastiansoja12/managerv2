package com.warehouse.terminal.domain.service;

import java.time.Duration;
import java.time.Instant;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.exception.DeviceAccessException;
import com.warehouse.terminal.domain.model.DeviceActivationCode;
import com.warehouse.terminal.domain.model.command.DevicePairingCodeCommand;
import com.warehouse.terminal.domain.port.secondary.DeviceActivationCodeRepository;
import com.warehouse.terminal.domain.vo.ActivationCode;
import com.warehouse.terminal.domain.vo.DeviceActivationCodeIssue;

public class DeviceActivationServiceImpl implements DeviceActivationService {

    private final DeviceActivationCodeRepository deviceActivationCodeRepository;

    private final Duration activationCodeValidity;

    public DeviceActivationServiceImpl(final DeviceActivationCodeRepository deviceActivationCodeRepository,
                                       final Duration activationCodeValidity) {
        this.deviceActivationCodeRepository = deviceActivationCodeRepository;
        this.activationCodeValidity = activationCodeValidity;
    }

    @Override
    public DeviceActivationCodeIssue issue(final DeviceId deviceId) {
        final ActivationCode activationCode = ActivationCode.generate();
        final DeviceActivationCode deviceActivationCode =
                DeviceActivationCode.issue(deviceId, activationCode.hash(), Instant.now(), activationCodeValidity);
        this.deviceActivationCodeRepository.deleteUnusedByDeviceId(deviceId);
        this.deviceActivationCodeRepository.save(deviceActivationCode);
        return new DeviceActivationCodeIssue(activationCode, deviceActivationCode.getExpiresAt());
    }

    @Override
    public DeviceActivationCodeIssue issueForUser(final DevicePairingCodeCommand command) {
        final ActivationCode activationCode = ActivationCode.generate();
        final DeviceActivationCode deviceActivationCode = DeviceActivationCode.issueForUser(command.userId(),
                command.operatorId(), command.departmentCode(), activationCode.hash(), Instant.now(),
                activationCodeValidity);
        this.deviceActivationCodeRepository.deleteUnusedByUserId(command.userId());
        this.deviceActivationCodeRepository.save(deviceActivationCode);
        return new DeviceActivationCodeIssue(activationCode, deviceActivationCode.getExpiresAt());
    }

    @Override
    public DeviceActivationCode consume(final ActivationCode activationCode) {
        final Instant now = Instant.now();
        return this.deviceActivationCodeRepository.findByCodeHash(activationCode.hash())
                .filter(code -> code.isUsable(now))
                .filter(code -> this.deviceActivationCodeRepository.markUsed(code.getActivationCodeId(), now))
                .orElseThrow(DeviceAccessException::invalidActivationCode);
    }

    @Override
    public void discardUnused(final DeviceId deviceId) {
        this.deviceActivationCodeRepository.deleteUnusedByDeviceId(deviceId);
    }
}
